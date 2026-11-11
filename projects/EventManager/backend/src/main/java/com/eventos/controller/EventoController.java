package com.eventos.controller;

import com.eventos.model.Evento;
import com.eventos.service.EventoService;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/eventos")
@CrossOrigin(origins = "http://localhost:5173")
public class EventoController {

    private final EventoService service;

    public EventoController(EventoService service) {
        this.service = service;
    }

    // Cadastrar evento
    @PostMapping
    public ResponseEntity<Evento> cadastrar(@RequestBody Evento evento) {
        Evento novoEvento = service.cadastrar(evento);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoEvento);
    }

    // Listar todos os eventos
    @GetMapping
    public ResponseEntity<List<Evento>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    // Buscar evento por ID
    @GetMapping("/{id}")
    public ResponseEntity<Evento> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // Atualizar evento
    @PutMapping("/{id}")
    public ResponseEntity<Evento> atualizar(
            @PathVariable Long id,
            @RequestBody Evento evento) {

        return ResponseEntity.ok(service.atualizar(id, evento));
    }

    // Excluir evento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);

        return ResponseEntity.noContent().build();
    }
}