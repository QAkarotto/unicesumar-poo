package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.dto.CriarOfertaRequest;
import br.edu.sistemaacademico.dto.OfertaDisciplinaResponse;
import br.edu.sistemaacademico.service.OfertaDisciplinaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/ofertas")
public class OfertaDisciplinaController {

    private final OfertaDisciplinaService service;

    public OfertaDisciplinaController(OfertaDisciplinaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<OfertaDisciplinaResponse> criar(@RequestBody CriarOfertaRequest request) {
        var oferta = service.criarOferta(request);
        return ResponseEntity.created(URI.create("/api/ofertas/" + oferta.id()))
                .body(oferta);
    }

    @GetMapping
    public ResponseEntity<List<OfertaDisciplinaResponse>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{identificador}")
    public ResponseEntity<OfertaDisciplinaResponse> buscar(@PathVariable("identificador") String identificador) {
        try {
            Long id = Long.parseLong(identificador);
            return ResponseEntity.ok(service.buscarPorId(id));
        } catch (NumberFormatException e) {
            return ResponseEntity.ok(service.buscarPorCodigo(identificador));
        }
    }
}
