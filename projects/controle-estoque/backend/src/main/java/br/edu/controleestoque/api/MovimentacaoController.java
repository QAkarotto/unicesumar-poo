package br.edu.controleestoque.api;

import br.edu.controleestoque.domain.TipoMovimentacao;
import br.edu.controleestoque.service.MovimentacaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

/**
 * Movimentações são imutáveis (histórico/auditoria): não há PUT nem DELETE.
 * Para corrigir um saldo usa-se uma nova movimentação do tipo AJUSTE.
 */
@RestController
@RequestMapping("/api/movimentacoes")
public class MovimentacaoController {

    private final MovimentacaoService service;

    public MovimentacaoController(MovimentacaoService service) {
        this.service = service;
    }

    /** GET /api/movimentacoes?produtoId=1&tipo=SAIDA */
    @GetMapping
    public List<MovimentacaoResponse> listar(@RequestParam(required = false) Long produtoId,
                                             @RequestParam(required = false) TipoMovimentacao tipo) {
        return service.listar(produtoId, tipo);
    }

    @GetMapping("/{id}")
    public MovimentacaoResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    public ResponseEntity<MovimentacaoResponse> registrar(@RequestBody @Valid MovimentacaoRequest req) {
        var registrada = service.registrar(req);
        var uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(registrada.id()).toUri();
        return ResponseEntity.created(uri).body(registrada);
    }
}
