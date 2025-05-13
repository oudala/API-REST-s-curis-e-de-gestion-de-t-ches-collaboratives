package net.ilyass.jpademo.service;

import net.ilyass.jpademo.entity.Task;
import net.ilyass.jpademo.entity.User;
import net.ilyass.jpademo.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void updateTaskStatus_ShouldUpdateStatus_WhenUserIsAssignedToTask() {
        Long taskId = 1L;
        String userEmail = "user@test.com";
        
        User assignedUser = new User();
        assignedUser.setId(1L);
        assignedUser.setEmail(userEmail);

        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setStatus(Task.TaskStatus.À_FAIRE);
        existingTask.setAssignedUser(assignedUser);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(userEmail);
        when(userService.findByEmail(userEmail)).thenReturn(Optional.of(assignedUser));
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(i -> i.getArguments()[0]);

        Task updatedTask = taskService.updateTaskStatus(taskId, Task.TaskStatus.EN_COURS);

        assertNotNull(updatedTask);
        assertEquals(Task.TaskStatus.EN_COURS, updatedTask.getStatus());
        verify(taskRepository).save(existingTask);
    }

    @Test
    void updateTaskStatus_ShouldThrowException_WhenUserIsNotAssignedToTask() {
        Long taskId = 1L;
        String userEmail = "user@test.com";
        
        User currentUser = new User();
        currentUser.setId(2L);
        currentUser.setEmail(userEmail);

        User assignedUser = new User();
        assignedUser.setId(1L);
        assignedUser.setEmail("other@test.com");

        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setAssignedUser(assignedUser);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(userEmail);
        when(userService.findByEmail(userEmail)).thenReturn(Optional.of(currentUser));
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            taskService.updateTaskStatus(taskId, Task.TaskStatus.EN_COURS);
        });

        assertEquals("Not authorizeed to update this task", exception.getMessage());
        verify(taskRepository, never()).save(any(Task.class));
    }
}
