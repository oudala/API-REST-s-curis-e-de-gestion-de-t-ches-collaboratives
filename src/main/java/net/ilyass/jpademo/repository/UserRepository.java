package net.ilyass.jpademo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import net.ilyass.jpademo.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}