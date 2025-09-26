package com.ifsc.tarefas.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ifsc.tarefas.model.Prioridade;
import com.ifsc.tarefas.model.Status;
import com.ifsc.tarefas.model.Tarefa;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> 
{
    List<Tarefa> findByTitulo(String titulo);
    List<Tarefa> findByStatus(Status status);
    List<Tarefa> findByResponsavel(String responsavel);
    List<Tarefa> findByDataLimiteBefore(LocalDate dataLimite);
    List<Tarefa> findByPrioridade(Prioridade prioridade);  
    List<Tarefa> findByStatusAndPrioridade(Status status, Prioridade prioridade);
} 