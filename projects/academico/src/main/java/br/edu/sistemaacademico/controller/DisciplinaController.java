package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.dto.CriarDisciplinaRequest;
import br.edu.sistemaacademico.dto.DisciplinaResponse;
import br.edu.sistemaacademico.service.DisciplinaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    private final DisciplinaService service;

    public DisciplinaController(DisciplinaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DisciplinaResponse> cadastrar(@RequestBody CriarDisciplinaRequest request) {
        var disciplina = service.cadastrar(request);
        return ResponseEntity.created(URI.create("/api/disciplinas/" + disciplina.codigo()))
                .body(disciplina);
    }

    @GetMapping
    public ResponseEntity<List<DisciplinaResponse>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{identificador}")
    public ResponseEntity<DisciplinaResponse> buscar(@PathVariable("identificador") String identificador) {
        try {
            Long id = Long.parseLong(identificador);
            return ResponseEntity.ok(service.buscarPorId(id));
        } catch (NumberFormatException e) {
            return ResponseEntity.ok(service.buscarPorCodigo(identificador));
        }
    }
}
