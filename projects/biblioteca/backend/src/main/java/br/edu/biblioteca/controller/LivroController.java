package br.edu.biblioteca.controller;

import br.edu.biblioteca.dto.*;
import br.edu.biblioteca.service.LivroService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/livros")
public class LivroController {
    private final LivroService service;
    public LivroController(LivroService service) { this.service = service; }
    @GetMapping public List<LivroResponse> listar() { return service.listar(); }
    @GetMapping("/{id}") public LivroResponse buscar(@PathVariable Long id) { return service.buscar(id); }
    @PostMapping public ResponseEntity<LivroResponse> criar(@Valid @RequestBody LivroRequest request) {
        var r = service.criar(request);
        return ResponseEntity.created(URI.create("/api/livros/" + r.id())).body(r);
    }
    @PutMapping("/{id}") public LivroResponse atualizar(@PathVariable Long id, @Valid @RequestBody LivroRequest request) { return service.atualizar(id, request); }
}
