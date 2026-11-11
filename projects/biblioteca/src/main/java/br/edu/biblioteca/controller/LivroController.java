package br.edu.biblioteca.controller;

import br.edu.biblioteca.domain.Livro;
import br.edu.biblioteca.dto.CriarLivroRequest;
import br.edu.biblioteca.dto.LivroResponse;
import br.edu.biblioteca.service.LivroService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/livros")
public class LivroController {

    private final LivroService service;

    public LivroController(LivroService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<LivroResponse> cadastrar(@RequestBody CriarLivroRequest request) {
        Livro livro = service.cadastrar(request.titulo(), request.autor(), request.isbn());
        return ResponseEntity
                .created(URI.create("/api/livros/" + livro.getId()))
                .body(LivroResponse.from(livro));
    }

    @GetMapping("/{id}")
    public LivroResponse buscar(@PathVariable("id") Long id) {
        return LivroResponse.from(service.buscar(id));
    }
}