package br.com.vetagenda.controller;
import br.com.vetagenda.dto.requests.AnimalRequest;
import br.com.vetagenda.service.AnimalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/animais")
public class AnimalController {
    private final AnimalService service;
    public AnimalController(AnimalService service) { this.service = service; }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Object cadastrar(@Valid @RequestBody AnimalRequest req) { return service.cadastrar(req); }
    @GetMapping public Object listar() { return service.listar(); }
    @GetMapping("/{id}") public Object buscar(@PathVariable Long id) { return service.buscar(id); }
    @PutMapping("/{id}") public Object atualizar(@PathVariable Long id, @Valid @RequestBody AnimalRequest req) { return service.atualizar(id, req); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) { service.excluir(id); }
}