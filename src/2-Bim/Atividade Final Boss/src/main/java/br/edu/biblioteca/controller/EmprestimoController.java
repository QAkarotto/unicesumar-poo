package br.edu.biblioteca.controller;

import br.edu.biblioteca.dto.CriarEmprestimoRequest;
import br.edu.biblioteca.dto.EmprestimoResponse;
import br.edu.biblioteca.service.EmprestimoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emprestimos")
@CrossOrigin(origins = "http://localhost:3000")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmprestimoResponse emprestar(@Valid @RequestBody CriarEmprestimoRequest request) {
        return EmprestimoResponse.from(emprestimoService.emprestar(request.leitorId(), request.livroId()));
    }

    @GetMapping
    public List<EmprestimoResponse> listar() {
        return emprestimoService.listar().stream().map(EmprestimoResponse::from).toList();
    }

    @GetMapping("/{id}")
    public EmprestimoResponse buscar(@PathVariable Long id) {
        return EmprestimoResponse.from(emprestimoService.buscarPorId(id));
    }

    @PostMapping("/{id}/devolucao")
    public EmprestimoResponse devolver(@PathVariable Long id) {
        return EmprestimoResponse.from(emprestimoService.devolver(id));
    }
}
