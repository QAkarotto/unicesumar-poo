package br.com.vetagenda.controller;
import br.com.vetagenda.dto.requests.AtendimentoRequest;
import br.com.vetagenda.service.AtendimentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/atendimentos")
public class AtendimentoController {
    private final AtendimentoService service;
    public AtendimentoController(AtendimentoService service) { this.service = service; }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Object cadastrar(@Valid @RequestBody AtendimentoRequest req) { return service.cadastrar(req); }
}