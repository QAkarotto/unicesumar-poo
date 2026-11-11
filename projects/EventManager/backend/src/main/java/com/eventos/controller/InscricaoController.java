
package com.eventos.controller;

import com.eventos.model.Inscricao;
import com.eventos.service.InscricaoService;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/inscricoes")
@CrossOrigin(origins = "http://localhost:5173")
public class InscricaoController {

    private final InscricaoService service;

    public InscricaoController(InscricaoService service) {
        this.service = service;
    }

    public record InscricaoRequest(
        Long eventoId,
        Long participanteId
    ) {}

    public record InscricaoResponse(
        Long id,
        Long eventoId,
        String eventoNome,
        Long participanteId,
        String participanteNome,
        LocalDateTime dataInscricao
    ) {
        public static InscricaoResponse from(Inscricao inscricao) {
            return new InscricaoResponse(
                inscricao.getId(),
                inscricao.getEvento().getId(),
                inscricao.getEvento().getNome(),
                inscricao.getParticipante().getId(),
                inscricao.getParticipante().getNome(),
                inscricao.getDataInscricao()
            );
        }
    }

    @PostMapping
    public ResponseEntity<InscricaoResponse> inscrever(
            @RequestBody InscricaoRequest request) {

        if (request == null ||
                request.eventoId() == null ||
                request.participanteId() == null) {
            return ResponseEntity.badRequest().build();
        }

        Inscricao inscricao = service.inscrever(
            request.eventoId(),
            request.participanteId()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(InscricaoResponse.from(inscricao));
    }

    @GetMapping
    public List<InscricaoResponse> listarTodos() {
        return service.listarTodos().stream()
            .map(InscricaoResponse::from)
            .toList();
    }

    @GetMapping("/evento/{eventoId}")
    public List<InscricaoResponse> listarPorEvento(
            @PathVariable Long eventoId) {

        return service.listarPorEvento(eventoId).stream()
            .map(InscricaoResponse::from)
            .toList();
    }

    @GetMapping("/participante/{participanteId}")
    public List<InscricaoResponse> listarPorParticipante(
            @PathVariable Long participanteId) {

        return service.listarPorParticipante(participanteId).stream()
            .map(InscricaoResponse::from)
            .toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        service.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}
