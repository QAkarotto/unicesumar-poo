package br.com.vetagenda.controller;
import br.com.vetagenda.dto.requests.AgendamentoRequest;
import br.com.vetagenda.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {
    private final AgendamentoService service;
    public AgendamentoController(AgendamentoService service) { this.service = service; }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Object cadastrar(@Valid @RequestBody AgendamentoRequest req) { return service.cadastrar(req); }
    @GetMapping public Object listar() { return service.listar(); }
    @GetMapping("/{id}") public Object buscar(@PathVariable Long id) { return service.buscar(id); }
    @PutMapping("/{id}") public Object atualizar(@PathVariable Long id, @Valid @RequestBody AgendamentoRequest req) { return service.atualizar(id, req); }
    @PatchMapping("/{id}/cancelar") public Object cancelar(@PathVariable Long id) { return service.cancelar(id); }
}