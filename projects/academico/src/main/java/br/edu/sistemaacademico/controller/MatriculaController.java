package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.service.SistemaAcademicoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final SistemaAcademicoService service;

    public MatriculaController(SistemaAcademicoService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarMatricula(@PathVariable Long id) {
        Matricula matricula = service.buscarMatricula(id);

        if (matricula == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(Map.of(
                "id", id,
                "ra", matricula.getAluno().getRa(),
                "aluno", matricula.getAluno().getNome(),
                "disciplina", matricula.getDisciplina().getCodigo(),
                "turma", matricula.getOferta().getTurma().getCodigo()
        ));
    }

    @PostMapping
    public ResponseEntity<?> criarMatricula(
            @RequestBody Map<String, String> dados
    ) {
        try {
            Long id = service.criarMatricula(
                    dados.get("ra"),
                    dados.get("codigoDisciplina")
            );

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("id", id));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", e.getMessage()));

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("erro", e.getMessage()));
        }
    }
}
