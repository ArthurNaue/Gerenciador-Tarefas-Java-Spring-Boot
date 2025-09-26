package com.ifsc.tarefas.services;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifsc.tarefas.model.Categoria;
import com.ifsc.tarefas.repository.CategoriaRepository;

@RestController
@RequestMapping("/categorias")
public class CategoriaService 
{
    private final CategoriaRepository repo;

    public CategoriaService(CategoriaRepository repo) 
    {
        this.repo = repo;
    }

    @PostMapping
    public ResponseEntity<Categoria> criarNovaCategoria(@RequestBody Categoria categoria)
    {
        if (repo.existsByNome(categoria.getNome())) 
        {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(repo.save(categoria));
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> listar() 
    {
        java.util.List<Categoria> categorias = repo.findAll();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscar(@PathVariable Long id) 
    {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> atualizar(@PathVariable Long id, @RequestBody Categoria dto) 
    {
        if (!repo.existsById(id)) 
        {
            return ResponseEntity.notFound().build();
        }
        
        Categoria categoria = repo.findById(id).orElse(null);
        if (categoria == null ) 
        {
            return ResponseEntity.badRequest().build();
        }
        
        categoria.setNome(dto.getNome());
        
        Categoria salvo = repo.save(categoria);
        return ResponseEntity.ok(salvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) 
    {
        if (!repo.existsById(id)) 
        {
            return ResponseEntity.notFound().build();
        }
        
        Categoria categoria = repo.findById(id).orElse(null);
        if (categoria != null && !categoria.getTarefas().isEmpty()) 
        {
            return ResponseEntity.badRequest().build();
        }
        
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
