package net.ilyass.jpademo.repository;

import net.ilyass.jpademo.entity.Task;
import net.ilyass.jpademo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignedUser(User user);
}