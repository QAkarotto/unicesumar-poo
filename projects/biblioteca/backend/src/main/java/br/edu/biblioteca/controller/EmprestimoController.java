package br.edu.biblioteca.controller;

import br.edu.biblioteca.dto.*;
import br.edu.biblioteca.service.EmprestimoService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoController {
    private final EmprestimoService service;
    public EmprestimoController(EmprestimoService service) { this.service = service; }
    @GetMapping public List<EmprestimoResponse> listar() { return service.listar(); }
    @GetMapping("/{id}") public EmprestimoResponse buscar(@PathVariable Long id) { return service.buscar(id); }
    @PostMapping public ResponseEntity<EmprestimoResponse> criar(@Valid @RequestBody EmprestimoRequest request) {
        var r = service.criar(request);
        return ResponseEntity.created(URI.create("/api/emprestimos/" + r.id())).body(r);
    }
    @PatchMapping("/{id}/devolucao") public EmprestimoResponse devolver(@PathVariable Long id) { return service.devolver(id); }
}
