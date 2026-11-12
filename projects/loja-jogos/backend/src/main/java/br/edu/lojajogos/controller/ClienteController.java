package br.edu.lojajogos.controller;

import br.edu.lojajogos.dto.ClienteResponse;
import br.edu.lojajogos.dto.CriarClienteRequest;
import br.edu.lojajogos.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "http://localhost:5173")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @GetMapping
    public List<ClienteResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ClienteResponse buscar(@PathVariable("id") Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> cadastrar(@RequestBody CriarClienteRequest request) {
        var criado = service.cadastrar(request);
        return ResponseEntity.created(URI.create("/api/clientes/" + criado.id())).body(criado);
    }
}