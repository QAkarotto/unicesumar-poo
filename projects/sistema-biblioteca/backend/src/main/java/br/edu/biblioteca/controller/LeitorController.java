
package br.edu.biblioteca.controller;

import br.edu.biblioteca.domain.Leitor;
import br.edu.biblioteca.dto.LeitorRequest;
import br.edu.biblioteca.dto.LeitorResponse;
import br.edu.biblioteca.service.LeitorService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/leitores")
public class LeitorController {

    private final LeitorService leitorService;

    public LeitorController(LeitorService leitorService) {
        this.leitorService = leitorService;
    }

    @PostMapping
    public ResponseEntity<LeitorResponse> cadastrar(
            @Valid @RequestBody LeitorRequest request) {

        Leitor leitor = new Leitor(
            request.nome(),
            request.email(),
            request.telefone()
        );

        Leitor salvo = leitorService.cadastrar(leitor);

        return ResponseEntity
            .created(URI.create("/api/leitores/" + salvo.getId()))
            .body(LeitorResponse.fromEntity(salvo));
    }

    @GetMapping
    public List<LeitorResponse> listar() {
        return leitorService.listar()
            .stream()
            .map(LeitorResponse::fromEntity)
            .toList();
    }

    @GetMapping("/{id}")
    public LeitorResponse buscarPorId(@PathVariable Long id) {
        return LeitorResponse.fromEntity(
            leitorService.buscarPorId(id)
        );
    }

    @PutMapping("/{id}")
    public LeitorResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody LeitorRequest request) {

        Leitor dados = new Leitor(
            request.nome(),
            request.email(),
            request.telefone()
        );

        return LeitorResponse.fromEntity(
            leitorService.atualizar(id, dados)
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        leitorService.excluir(id);
    }
}
