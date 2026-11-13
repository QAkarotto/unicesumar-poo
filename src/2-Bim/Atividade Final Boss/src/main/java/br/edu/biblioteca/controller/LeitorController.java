package br.edu.biblioteca.controller;

import br.edu.biblioteca.dto.LeitorRequest;
import br.edu.biblioteca.dto.LeitorResponse;
import br.edu.biblioteca.service.LeitorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leitores")
@CrossOrigin(origins = "http://localhost:3000")
public class LeitorController {

    private final LeitorService leitorService;

    public LeitorController(LeitorService leitorService) {
        this.leitorService = leitorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LeitorResponse cadastrar(@Valid @RequestBody LeitorRequest request) {
        return LeitorResponse.from(leitorService.cadastrar(request.nome(), request.email()));
    }

    @GetMapping
    public List<LeitorResponse> listar() {
        return leitorService.listar().stream().map(LeitorResponse::from).toList();
    }

    @GetMapping("/{id}")
    public LeitorResponse buscar(@PathVariable Long id) {
        return LeitorResponse.from(leitorService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public LeitorResponse atualizar(@PathVariable Long id, @Valid @RequestBody LeitorRequest request) {
        return LeitorResponse.from(leitorService.atualizar(id, request.nome(), request.email()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        leitorService.excluir(id);
    }
}
