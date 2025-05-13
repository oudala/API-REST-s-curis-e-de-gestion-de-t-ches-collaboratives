package net.ilyass.jpademo.service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.ilyass.jpademo.dto.TaskRequest;
import net.ilyass.jpademo.entity.Task;
import net.ilyass.jpademo.entity.Task.TaskStatus; 
import net.ilyass.jpademo.entity.User;
import net.ilyass.jpademo.repository.TaskRepository;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getUserTasks() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User mkynchy"));
        return taskRepository.findByAssignedUser(user);
    }

    public Task createTask(TaskRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User mkynch"));

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setAssignedUser(user);

        return taskRepository.save(task);
    }

    public Task updateTaskStatus(Long taskId, TaskStatus newStatus) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User mkynch"));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task mkynach"));

        if (!task.getAssignedUser().getId().equals(user.getId())) {
            throw new RuntimeException("mkynch 3ndk l7e9 dir update");
        }

        task.setStatus(newStatus);
        return taskRepository.save(task);
    }
}