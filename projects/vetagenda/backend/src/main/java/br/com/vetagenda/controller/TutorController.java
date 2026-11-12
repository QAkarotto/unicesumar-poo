package br.com.vetagenda.controller;
import br.com.vetagenda.dto.requests.TutorRequest;
import br.com.vetagenda.service.TutorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/tutores")
public class TutorController {
    private final TutorService service;
    public TutorController(TutorService service) { this.service = service; }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Object cadastrar(@Valid @RequestBody TutorRequest req) { return service.cadastrar(req); }
    @GetMapping public Object listar() { return service.listar(); }
    @GetMapping("/{id}") public Object buscar(@PathVariable Long id) { return service.buscar(id); }
    @PutMapping("/{id}") public Object atualizar(@PathVariable Long id, @Valid @RequestBody TutorRequest req) { return service.atualizar(id, req); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) { service.excluir(id); }
}