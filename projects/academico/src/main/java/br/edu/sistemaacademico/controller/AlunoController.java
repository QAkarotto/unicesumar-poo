
package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.service.SistemaAcademicoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<Aluno> cadastrarAluno(
            @RequestBody Aluno aluno) {

        if (service.buscarAluno(aluno.getRa()) != null) {
            return ResponseEntity.status(409).build();
        }

        Aluno alunoSalvo = service.salvarAluno(aluno);

        return ResponseEntity.status(201).body(alunoSalvo);
    }
}
