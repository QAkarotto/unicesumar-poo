
package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.repository.DadosAcademicos;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final DadosAcademicos dadosAcademicos;

    public AlunoController(DadosAcademicos dadosAcademicos) {
        this.dadosAcademicos = dadosAcademicos;
    }

    @GetMapping("/{identificador}")
    public ResponseEntity<?> buscarAluno(
            @PathVariable String identificador
    ) {
        return dadosAcademicos.buscarAluno(identificador)
                .<ResponseEntity<?>>map(aluno -> ResponseEntity.ok(
                        Map.of(
                                "registroAcademico", aluno.getRegistroAcademico(),
                                "nome", aluno.getNome(),
                                "email", aluno.getEmail()
                        )
                ))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
