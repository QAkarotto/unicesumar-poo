package br.edu.estoque.controller;

import br.edu.estoque.dto.*;
import br.edu.estoque.service.ProdutoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {
    private final ProdutoService service;
    public ProdutoController(ProdutoService service) { this.service = service; }

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public ProdutoResponse criar(@Valid @RequestBody ProdutoRequest r) { return ProdutoResponse.de(service.criar(r)); }

    @GetMapping
    public List<ProdutoResponse> listar() { return service.listar().stream().map(ProdutoResponse::de).toList(); }

    @GetMapping("/baixo-estoque")
    public List<ProdutoResponse> baixoEstoque() { return service.abaixoDoMinimo().stream().map(ProdutoResponse::de).toList(); }

    @GetMapping("/{id}")
    public ProdutoResponse buscar(@PathVariable Long id) { return ProdutoResponse.de(service.buscar(id)); }

    @PutMapping("/{id}")
    public ProdutoResponse atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoRequest r) {
        return ProdutoResponse.de(service.atualizar(id, r));
    }

    @PatchMapping("/{id}/desativacao")
    public ProdutoResponse desativar(@PathVariable Long id) { return ProdutoResponse.de(service.desativar(id)); }
}
