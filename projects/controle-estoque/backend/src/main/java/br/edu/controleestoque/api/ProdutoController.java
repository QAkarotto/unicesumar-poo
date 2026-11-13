package br.edu.controleestoque.api;

import br.edu.controleestoque.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    /** GET /api/produtos?categoriaId=1&busca=mouse */
    @GetMapping
    public List<ProdutoResponse> listar(@RequestParam(required = false) Long categoriaId,
                                        @RequestParam(required = false) String busca) {
        return service.listar(categoriaId, busca);
    }

    @GetMapping("/{id}")
    public ProdutoDetalheResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @GetMapping("/reposicao")
    public List<ProdutoResponse> paraReposicao() {
        return service.paraReposicao();
    }

    @GetMapping("/resumo")
    public ResumoEstoqueResponse resumo() {
        return service.resumo();
    }

    @PostMapping
    public ResponseEntity<ProdutoResponse> criar(@RequestBody @Valid ProdutoRequest req) {
        var criado = service.criar(req);
        var uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(uri).body(criado);
    }

    @PutMapping("/{id}")
    public ProdutoResponse atualizar(@PathVariable Long id, @RequestBody @Valid ProdutoRequest req) {
        return service.atualizar(id, req);
    }

    @PatchMapping("/{id}/inativacao")
    public ProdutoResponse inativar(@PathVariable Long id) {
        return service.inativar(id);
    }

    @PatchMapping("/{id}/ativacao")
    public ProdutoResponse ativar(@PathVariable Long id) {
        return service.ativar(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }

    @PostMapping("/{id}/fornecedores/{fornecedorId}")
    public ProdutoDetalheResponse vincularFornecedor(@PathVariable Long id, @PathVariable Long fornecedorId) {
        return service.vincularFornecedor(id, fornecedorId);
    }

    @DeleteMapping("/{id}/fornecedores/{fornecedorId}")
    public ProdutoDetalheResponse desvincularFornecedor(@PathVariable Long id, @PathVariable Long fornecedorId) {
        return service.desvincularFornecedor(id, fornecedorId);
    }
}
