
package br.edu.biblioteca.controller;

import br.edu.biblioteca.dto.EmprestimoRequest;
import br.edu.biblioteca.dto.EmprestimoResponse;
import br.edu.biblioteca.service.EmprestimoService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController(
            EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @PostMapping
    public ResponseEntity<EmprestimoResponse> emprestar(
            @Valid @RequestBody EmprestimoRequest request) {

        EmprestimoResponse resposta =
            emprestimoService.emprestarResponse(
                request.livroId(),
                request.leitorId()
            );

        return ResponseEntity
            .created(URI.create(
                "/api/emprestimos/" + resposta.id()
            ))
            .body(resposta);
    }

    @GetMapping
    public List<EmprestimoResponse> listar() {
        return emprestimoService.listarResponse();
    }

    @GetMapping("/{id}")
    public EmprestimoResponse buscarPorId(
            @PathVariable Long id) {
        return emprestimoService.buscarResponse(id);
    }

    @GetMapping("/leitor/{leitorId}")
    public List<EmprestimoResponse> listarPorLeitor(
            @PathVariable Long leitorId) {
        return emprestimoService.listarPorLeitorResponse(
            leitorId
        );
    }

    @GetMapping("/livro/{livroId}")
    public List<EmprestimoResponse> listarPorLivro(
            @PathVariable Long livroId) {
        return emprestimoService.listarPorLivroResponse(
            livroId
        );
    }

    @PatchMapping("/{id}/devolucao")
    public EmprestimoResponse devolver(@PathVariable Long id) {
        return emprestimoService.devolverResponse(id);
    }
}
