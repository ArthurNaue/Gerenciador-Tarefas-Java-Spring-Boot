package com.ifsc.tarefas.services;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifsc.tarefas.model.Prioridade;
import com.ifsc.tarefas.model.Status;
import com.ifsc.tarefas.model.Tarefa;
import com.ifsc.tarefas.repository.CategoriaRepository;
import com.ifsc.tarefas.repository.TarefaRepository;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/tarefas")
public class TarefaServices 
{
    private final TarefaRepository tarefaRepository;
    private final CategoriaRepository categoriaRepository;

    public TarefaServices(TarefaRepository tarefaRepository, CategoriaRepository categoriaRepository) 
    {
        this.tarefaRepository = tarefaRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping("/buscar-todos")
    public ResponseEntity<?> buscarTodas()
    {
        return ResponseEntity.ok(tarefaRepository.findAll());
    }
    
    @PostMapping("/inserir")
    public ResponseEntity<Tarefa> criarNovaTarefa(@RequestBody Tarefa tarefa)
    {
        return ResponseEntity.ok(tarefaRepository.save(tarefa));
    }

    @PutMapping("editar/{id}")
    public ResponseEntity<Tarefa> editarTarefa(@PathVariable Long id, @RequestBody Tarefa novaTarefa) 
    {
        return tarefaRepository.findById(id).map
        (
            tarefa -> 
            {
                tarefa.setTitulo(novaTarefa.getTitulo());
                tarefa.setDescricao(novaTarefa.getDescricao());
                tarefa.setResponsavel(novaTarefa.getResponsavel());
                tarefa.setDataLimite(novaTarefa.getDataLimite());
                tarefa.setStatus(novaTarefa.getStatus());
                tarefa.setPrioridade(novaTarefa.getPrioridade());
                return ResponseEntity.ok(tarefaRepository.save(tarefa));
            }
        ).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("deletar/{id}")
    public ResponseEntity<Tarefa> deletarTarefa(@PathVariable Long id) 
    {
        if(!tarefaRepository.existsById(id))
        {
            return ResponseEntity.notFound().build();
        }

        tarefaRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/{tarefaId}/associar-categoria/{categoriaId}")
    @Transactional
    public ResponseEntity<Void> associarTarefaParaUmaCategoria
    (
        @PathVariable Long tarefaId,
        @PathVariable Long categoriaId
    )
    {
        var tarefa = tarefaRepository.findById(tarefaId);
        var categoria = categoriaRepository.findById(categoriaId);

        if(tarefa.isEmpty() || categoria.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }

        tarefa.get().getCategorias().add(categoria.get());
        tarefaRepository.save(tarefa.get());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/por-titulo/{titulo}")
    public ResponseEntity<List<Tarefa>> buscarPorTitulo(@PathVariable String titulo) 
    {
        return ResponseEntity.ok(tarefaRepository.findByTitulo(titulo));
    }

    @GetMapping("/por-status/{status}")
    public ResponseEntity<List<Tarefa>> buscarPorStatus(@PathVariable Status status) 
    {
        return ResponseEntity.ok(tarefaRepository.findByStatus(status));
    }

    @GetMapping("/por-responsavel/{responsavel}")
    public ResponseEntity<List<Tarefa>> buscarPorResponsavel(@PathVariable String responsavel) 
    {
        return ResponseEntity.ok(tarefaRepository.findByResponsavel(responsavel));
    }

    @GetMapping("/por-prioridade/{prioridade}")
    public ResponseEntity<List<Tarefa>> buscarPorPrioridade(@PathVariable Prioridade prioridade) 
    {
        return ResponseEntity.ok(tarefaRepository.findByPrioridade(prioridade));
    }

    @GetMapping("/vencidas")
    public ResponseEntity<List<Tarefa>> buscarTarefasVencidas() 
    {
        return ResponseEntity.ok(tarefaRepository.findByDataLimiteBefore(LocalDate.now()));
    }
}
