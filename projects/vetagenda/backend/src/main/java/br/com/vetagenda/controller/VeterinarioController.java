package br.com.vetagenda.controller;
import br.com.vetagenda.dto.requests.VeterinarioRequest;
import br.com.vetagenda.service.VeterinarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/veterinarios")
public class VeterinarioController {
    private final VeterinarioService service;
    public VeterinarioController(VeterinarioService service) { this.service = service; }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Object cadastrar(@Valid @RequestBody VeterinarioRequest req) { return service.cadastrar(req); }
    @GetMapping public Object listar() { return service.listar(); }
}