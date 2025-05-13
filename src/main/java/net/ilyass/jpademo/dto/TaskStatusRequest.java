package net.ilyass.jpademo.dto;

import lombok.Data;
import net.ilyass.jpademo.entity.Task.TaskStatus;

@Data
public class TaskStatusRequest {
    private TaskStatus status;
}
