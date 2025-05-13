package net.ilyass.jpademo.dto;

import lombok.Data;
import net.ilyass.jpademo.entity.Task.TaskStatus;

@Data
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private UserDto assignedUser;

    @Data
    public static class UserDto {
        private Long id;
        private String name;
        private String email;
    }
}
