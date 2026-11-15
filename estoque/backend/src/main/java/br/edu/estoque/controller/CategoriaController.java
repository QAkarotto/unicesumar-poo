package br.edu.estoque.controller;

import br.edu.estoque.dto.*;
import br.edu.estoque.service.CategoriaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {
    private final CategoriaService service;
    public CategoriaController(CategoriaService service) { this.service = service; }

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public CategoriaResponse criar(@Valid @RequestBody CategoriaRequest r) { return CategoriaResponse.de(service.criar(r.nome())); }

    @GetMapping
    public List<CategoriaResponse> listar() { return service.listar().stream().map(CategoriaResponse::de).toList(); }

    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) { service.excluir(id); }
}
