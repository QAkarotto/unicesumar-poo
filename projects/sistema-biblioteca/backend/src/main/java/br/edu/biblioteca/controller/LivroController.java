
package br.edu.biblioteca.controller;

import br.edu.biblioteca.domain.Livro;
import br.edu.biblioteca.dto.LivroRequest;
import br.edu.biblioteca.dto.LivroResponse;
import br.edu.biblioteca.service.LivroService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @PostMapping
    public ResponseEntity<LivroResponse> cadastrar(
            @Valid @RequestBody LivroRequest request) {

        Livro livro = new Livro(
            request.titulo(),
            request.autor(),
            request.isbn(),
            request.quantidadeTotal(),
            request.quantidadeTotal()
        );

        Livro salvo = livroService.cadastrar(livro);

        return ResponseEntity
            .created(URI.create("/api/livros/" + salvo.getId()))
            .body(LivroResponse.fromEntity(salvo));
    }

    @GetMapping
    public List<LivroResponse> listar() {
        return livroService.listar()
            .stream()
            .map(LivroResponse::fromEntity)
            .toList();
    }

    @GetMapping("/{id}")
    public LivroResponse buscarPorId(@PathVariable Long id) {
        return LivroResponse.fromEntity(
            livroService.buscarPorId(id)
        );
    }

    @PutMapping("/{id}")
    public LivroResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody LivroRequest request) {

        Livro dados = new Livro(
            request.titulo(),
            request.autor(),
            request.isbn(),
            request.quantidadeTotal(),
            request.quantidadeTotal()
        );

        return LivroResponse.fromEntity(
            livroService.atualizar(id, dados)
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        livroService.excluir(id);
    }
}
