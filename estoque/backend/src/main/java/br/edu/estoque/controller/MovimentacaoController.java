package br.edu.estoque.controller;

import br.edu.estoque.dto.*;
import br.edu.estoque.service.MovimentacaoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movimentacoes")
public class MovimentacaoController {
    private final MovimentacaoService service;
    public MovimentacaoController(MovimentacaoService service) { this.service = service; }

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public MovimentacaoResponse registrar(@Valid @RequestBody MovimentacaoRequest r) {
        return MovimentacaoResponse.de(service.registrar(r));
    }

    @GetMapping
    public List<MovimentacaoResponse> listar(@RequestParam(required = false) Long produtoId) {
        return service.listar(produtoId).stream().map(MovimentacaoResponse::de).toList();
    }
}
