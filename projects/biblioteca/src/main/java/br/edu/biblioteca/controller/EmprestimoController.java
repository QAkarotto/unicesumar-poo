
package br.edu.biblioteca.controller;

import br.edu.biblioteca.domain.Emprestimo;
import br.edu.biblioteca.dto.CriarEmprestimoRequest;
import br.edu.biblioteca.dto.EmprestimoResponse;
import br.edu.biblioteca.service.EmprestimoService;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoController {

    private final EmprestimoService service;

    public EmprestimoController(EmprestimoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EmprestimoResponse> emprestar(@RequestBody CriarEmprestimoRequest request) {
        Emprestimo emprestimo = service.emprestar(request.usuarioId(), request.livroId());
        return ResponseEntity
                .created(URI.create("/api/emprestimos/" + emprestimo.getId()))
                .body(EmprestimoResponse.from(emprestimo));
    }

    @GetMapping
    public List<EmprestimoResponse> listar() {
        return service.listar().stream()
                .map(EmprestimoResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public EmprestimoResponse buscar(@PathVariable("id") Long id) {
        return EmprestimoResponse.from(service.buscar(id));
    }

    @PatchMapping("/{id}/devolucao")
    public EmprestimoResponse devolver(@PathVariable("id") Long id) {
        return EmprestimoResponse.from(service.devolver(id));
    }
}
