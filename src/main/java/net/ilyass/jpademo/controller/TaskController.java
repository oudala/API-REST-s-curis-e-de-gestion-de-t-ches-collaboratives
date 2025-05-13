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

import lombok.RequiredArgsConstructor;
import net.ilyass.jpademo.dto.TaskRequest;
import net.ilyass.jpademo.dto.TaskResponse;
import net.ilyass.jpademo.dto.TaskStatusRequest;
import net.ilyass.jpademo.entity.Task;
import net.ilyass.jpademo.service.TaskService;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

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

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Task>> getUserTasks() {
        return ResponseEntity.ok(taskService.getUserTasks());
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskService.createTask(request));
    }

    @PutMapping("/{taskId}/status")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Task> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestBody TaskStatusRequest request) {
        return ResponseEntity.ok(taskService.updateTaskStatus(taskId, request.getStatus()));
    }
}