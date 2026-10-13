
package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.dto.AlunoResponse;
import br.edu.sistemaacademico.service.AcademicoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                .map(aluno -> ResponseEntity.ok(
                        new AlunoResponse(
                                aluno.getRegistroAcademico(),
                                aluno.getNome(),
                                aluno.getEmail()
                        )
                ))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
