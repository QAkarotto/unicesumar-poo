package br.com.vetagenda.controller;
import br.com.vetagenda.dto.requests.EspecialidadeRequest;
import br.com.vetagenda.service.EspecialidadeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/especialidades")
public class EspecialidadeController {
    private final EspecialidadeService service;
    public EspecialidadeController(EspecialidadeService service) { this.service = service; }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Object cadastrar(@Valid @RequestBody EspecialidadeRequest req) { return service.cadastrar(req); }
    @GetMapping public Object listar() { return service.listar(); }
}