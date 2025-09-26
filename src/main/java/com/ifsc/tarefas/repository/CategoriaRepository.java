package com.ifsc.tarefas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ifsc.tarefas.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> 
{
    Optional<Categoria> findByNome(String nome);
    boolean existsByNome(String nome);
} 