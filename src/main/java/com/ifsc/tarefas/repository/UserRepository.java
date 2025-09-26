package com.ifsc.tarefas.repository;

import com.ifsc.tarefas.model.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> 
{
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}