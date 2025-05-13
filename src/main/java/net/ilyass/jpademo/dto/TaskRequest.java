package net.ilyass.jpademo.dto;

import lombok.Data;
import net.ilyass.jpademo.entity.Task.TaskStatus;

@Data
public class TaskRequest {
    private String title;
    private String description;
    private TaskStatus status = TaskStatus.À_FAIRE; 
}
