
package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.dto.AlunoRequest;
import br.edu.sistemaacademico.dto.AlunoResponse;
import br.edu.sistemaacademico.service.AcademicoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AcademicoService academicoService;

    public AlunoController(AcademicoService academicoService) {
        this.academicoService = academicoService;
    }

    @GetMapping("/{identificador}")
    public ResponseEntity<AlunoResponse> buscarAluno(
            @PathVariable String identificador) {

        return academicoService.buscarAluno(identificador)
                .map(aluno -> ResponseEntity.ok(converter(aluno)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> cadastrarAluno(
            @RequestBody AlunoRequest request) {

        try {
            Aluno aluno = academicoService.cadastrarAluno(request);

            return ResponseEntity
                    .created(URI.create("/api/alunos/"
                            + aluno.getRegistroAcademico()))
                    .body(converter(aluno));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409)
                    .body(Map.of("erro", e.getMessage()));

        } catch (IllegalStateException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("erro", e.getMessage()));
        }
    }

    private AlunoResponse converter(Aluno aluno) {
        return new AlunoResponse(
                aluno.getRegistroAcademico(),
                aluno.getNome(),
                aluno.getEmail()
        );
    }
}
