package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.dto.ConcluirMatriculaRequest;
import br.edu.sistemaacademico.dto.CriarMatriculaRequest;
import br.edu.sistemaacademico.dto.MatriculaResponse;
import br.edu.sistemaacademico.service.MatriculaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final MatriculaService service;

    public MatriculaController(MatriculaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<MatriculaResponse> matricular(@RequestBody CriarMatriculaRequest request) {
        var matricula = service.matricular(request);
        return ResponseEntity.created(URI.create("/api/matriculas/" + matricula.codigo()))
                .body(matricula);
    }

    @GetMapping
    public ResponseEntity<List<MatriculaResponse>> listar(
            @RequestParam(name = "ra", required = false) String ra) {
        if (ra != null && !ra.isBlank()) {
            return ResponseEntity.ok(service.listarPorAluno(ra));
        }
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{identificador}")
    public ResponseEntity<MatriculaResponse> buscar(@PathVariable("identificador") String identificador) {
        try {
            Long id = Long.parseLong(identificador);
            return ResponseEntity.ok(service.buscarPorId(id));
        } catch (NumberFormatException e) {
            return ResponseEntity.ok(service.buscarPorCodigo(identificador));
        }
    }

    @PatchMapping("/{id}/conclusao")
    public ResponseEntity<MatriculaResponse> concluir(
            @PathVariable("id") Long id,
            @RequestBody ConcluirMatriculaRequest request) {
        return ResponseEntity.ok(service.concluir(id, request));
    }

    @PatchMapping("/{id}/trancar")
    public ResponseEntity<MatriculaResponse> trancar(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.trancar(id));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<MatriculaResponse> cancelar(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.cancelar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable("id") Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
