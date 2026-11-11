
package com.eventos.controller;

import com.eventos.model.Participante;
import com.eventos.service.ParticipanteService;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/participantes")
@CrossOrigin(origins = "http://localhost:5173")
public class ParticipanteController {

    private final ParticipanteService service;

    public ParticipanteController(ParticipanteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Participante> cadastrar(
            @RequestBody Participante participante) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.cadastrar(participante));
    }

    @GetMapping
    public List<Participante> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public Participante buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Participante atualizar(
            @PathVariable Long id,
            @RequestBody Participante participante) {

        return service.atualizar(id, participante);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
