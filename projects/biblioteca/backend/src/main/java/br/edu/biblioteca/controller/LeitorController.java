package br.edu.biblioteca.controller;

import br.edu.biblioteca.dto.*;
import br.edu.biblioteca.service.LeitorService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leitores")
public class LeitorController {
    private final LeitorService service;
    public LeitorController(LeitorService service) { this.service = service; }
    @GetMapping public List<LeitorResponse> listar() { return service.listar(); }
    @GetMapping("/{id}") public LeitorResponse buscar(@PathVariable Long id) { return service.buscar(id); }
    @PostMapping public ResponseEntity<LeitorResponse> criar(@Valid @RequestBody LeitorRequest request) {
        var r = service.criar(request);
        return ResponseEntity.created(URI.create("/api/leitores/" + r.id())).body(r);
    }
    @PutMapping("/{id}") public LeitorResponse atualizar(@PathVariable Long id, @Valid @RequestBody LeitorRequest request) { return service.atualizar(id, request); }
    @PatchMapping("/{id}/atividade") public LeitorResponse atividade(@PathVariable Long id, @Valid @RequestBody AtividadeRequest request) { return service.alterarAtividade(id, request.ativo()); }
}
