
package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.dto.MatriculaRequest;
import br.edu.sistemaacademico.dto.MatriculaResponse;
import br.edu.sistemaacademico.service.AcademicoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final AcademicoService academicoService;

    public MatriculaController(AcademicoService academicoService) {
        this.academicoService = academicoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatriculaResponse> buscarMatricula(
            @PathVariable String id) {

        return academicoService.buscarMatricula(id)
                .map(matricula -> ResponseEntity.ok(converter(matricula)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criarMatricula(
            @RequestBody MatriculaRequest request) {

        try {
            Matricula matricula = academicoService.matricular(
                    request.identificadorAluno(),
                    request.codigoTurma(),
                    request.codigoDisciplina()
            );

            return ResponseEntity
                    .created(URI.create("/api/matriculas/" + matricula.getCodigo()))
                    .body(converter(matricula));

        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(java.util.Map.of("erro", e.getMessage()));
        }
    }

    private MatriculaResponse converter(Matricula matricula) {
        return new MatriculaResponse(
                matricula.getCodigo(),
                matricula.getAluno().getRegistroAcademico(),
                matricula.getAluno().getNome(),
                matricula.getTurma().getCodigo(),
                matricula.getOfertaDisciplina().getDisciplina().getCodigo(),
                matricula.getSituacao().name()
        );
    }
}
