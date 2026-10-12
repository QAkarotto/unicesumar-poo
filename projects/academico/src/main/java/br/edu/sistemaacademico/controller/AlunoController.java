package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.service.SistemaAcademicoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final SistemaAcademicoService service;

    public AlunoController(SistemaAcademicoService service) {
        this.service = service;
    }

    @GetMapping("/{identificador}")
    public ResponseEntity<Aluno> buscarAluno(
            @PathVariable String identificador) {

        Aluno aluno = service.buscarAluno(identificador);

        if (aluno == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(aluno);
    }
}
