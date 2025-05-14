package net.ilyass.jpademo.dto;

import lombok.Data;
import net.ilyass.jpademo.entity.Task.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Schema(description = """
    Request object for creating tasks.
    Note: assignedUserId is only considered when request is made by ADMIN users.
    Normal users can only create tasks for themselves.
    """)
public class TaskRequest {
    @Schema(description = "Task title", example = "Complete project documentation")
    @NotBlank(message = "add your task like pfa task :)")
    private String title;
    
    @Schema(description = "Detailed task description", example = "Write technical documentation for the API endpoints")
    @NotBlank(message = "Description cannot be blank")
    private String description;
    
    @Schema(description = "Current status of the task", example = "À_FAIRE")
    @NotNull(message = "Status cannot be null")
    private TaskStatus status;
    
    @Schema(description = "ID of user to assign task to (ADMIN only)", example = "2")
    private Long assignedUserId; // Optional, only used by admin
}
