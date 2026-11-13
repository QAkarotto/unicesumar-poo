package br.edu.biblioteca.controller;

import br.edu.biblioteca.dto.AtualizarLivroRequest;
import br.edu.biblioteca.dto.CriarLivroRequest;
import br.edu.biblioteca.dto.LivroResponse;
import br.edu.biblioteca.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livros")
@CrossOrigin(origins = "http://localhost:3000")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LivroResponse cadastrar(@Valid @RequestBody CriarLivroRequest request) {
        return LivroResponse.from(livroService.cadastrar(
                request.isbn(), request.titulo(), request.autor(), request.quantidadeTotal()));
    }

    @GetMapping
    public List<LivroResponse> listar() {
        return livroService.listar().stream().map(LivroResponse::from).toList();
    }

    @GetMapping("/{id}")
    public LivroResponse buscar(@PathVariable Long id) {
        return LivroResponse.from(livroService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public LivroResponse atualizar(@PathVariable Long id, @Valid @RequestBody AtualizarLivroRequest request) {
        return LivroResponse.from(livroService.atualizar(id, request.titulo(), request.autor()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        livroService.excluir(id);
    }
}
