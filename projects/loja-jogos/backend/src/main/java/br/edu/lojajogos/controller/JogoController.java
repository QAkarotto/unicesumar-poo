package br.edu.lojajogos.controller;

import br.edu.lojajogos.dto.JogoRequest;
import br.edu.lojajogos.dto.JogoResponse;
import br.edu.lojajogos.service.JogoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/jogos")
@CrossOrigin(origins = "http://localhost:5173")
public class JogoController {

    private final JogoService service;

    public JogoController(JogoService service) {
        this.service = service;
    }

    @GetMapping
    public List<JogoResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public JogoResponse buscar(@PathVariable("id") Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<JogoResponse> cadastrar(@RequestBody JogoRequest request) {
        var criado = service.cadastrar(request);
        return ResponseEntity.created(URI.create("/api/jogos/" + criado.id())).body(criado);
    }

    @PutMapping("/{id}")
    public JogoResponse atualizar(@PathVariable("id") Long id, @RequestBody JogoRequest request) {
        return service.atualizar(id, request);
    }
}