package net.ilyass.jpademo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.ilyass.jpademo.dto.TaskRequest;
import net.ilyass.jpademo.dto.TaskResponse;
import net.ilyass.jpademo.dto.TaskStatusRequest;
import net.ilyass.jpademo.entity.Task;
import net.ilyass.jpademo.service.TaskService;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Task Management", description = "API endpoints for managing user tasks")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Get all tasks", description = "Retrieves all tasks in the system. Only accessible by admins.")
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        List<TaskResponse> responses = tasks.stream()
            .map(this::mapToTaskResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    private TaskResponse mapToTaskResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        
        if (task.getAssignedUser() != null) {
            TaskResponse.UserDto userDto = new TaskResponse.UserDto();
            userDto.setId(task.getAssignedUser().getId());
            userDto.setName(task.getAssignedUser().getName());
            userDto.setEmail(task.getAssignedUser().getEmail());
            response.setAssignedUser(userDto);
        }
        
        return response;
    }

    @Operation(summary = "Get user's tasks", description = "Retrieves all tasks assigned to the currently logged-in user")
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Task>> getUserTasks() {
        return ResponseEntity.ok(taskService.getUserTasks());
    }

    @Operation(summary = "Create a new task", description = """
        Creates a new task with different behavior based on user role:
        
        For ADMIN users:
        - Can create tasks and assign them to any user
        - Must include assignedUserId in request to assign to specific user
        - If no assignedUserId provided, task is assigned to admin
        
        For normal users:
        - Can only create tasks assigned to themselves
        - assignedUserId is ignored if provided
        
        Example for ADMIN:
        {
          "title": "stage pfe",
          "description": "final task",
          "status": "À_FAIRE",
          "assignedUserId": 2
        }
        
        Example for normal user:
        {
          "title": "student",
          "description": "stage pfe", 
          "status": "À_FAIRE"
        }
        """)
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody @Valid TaskRequest request) {
        return ResponseEntity.ok(taskService.createTask(request));
    }

    @Operation(summary = "Update task status", 
              description = "Updates the status of a task. Only the assigned user can update their own tasks.")
    @PutMapping("/{taskId}/status")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Task> updateTaskStatus(
            @Parameter(description = "ID of the task to update") @PathVariable Long taskId,
            @RequestBody TaskStatusRequest request) {
        return ResponseEntity.ok(taskService.updateTaskStatus(taskId, request.getStatus()));
    }
}