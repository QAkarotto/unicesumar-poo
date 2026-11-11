package br.edu.biblioteca.controller;

import br.edu.biblioteca.domain.Usuario;
import br.edu.biblioteca.dto.CriarUsuarioRequest;
import br.edu.biblioteca.dto.UsuarioResponse;
import br.edu.biblioteca.service.UsuarioService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrar(@RequestBody CriarUsuarioRequest request) {
        Usuario usuario = service.cadastrar(request.nome(), request.email());
        return ResponseEntity
                .created(URI.create("/api/usuarios/" + usuario.getId()))
                .body(UsuarioResponse.from(usuario));
    }

    @GetMapping("/{id}")
    public UsuarioResponse buscar(@PathVariable("id") Long id) {
        return UsuarioResponse.from(service.buscar(id));
    }
}