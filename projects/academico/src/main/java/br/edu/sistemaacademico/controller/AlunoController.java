package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.DadosAcademicos;
import br.edu.sistemaacademico.domain.Aluno;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final DadosAcademicos dados;

    public AlunoController(DadosAcademicos dados) {
        this.dados = dados;
    }

    @GetMapping("/{identificador}")
    public ResponseEntity<?> consultarAluno(
            @PathVariable String identificador) {

        Aluno aluno = dados.buscarAluno(identificador);

        if (aluno == null) {
            return ResponseEntity.status(404)
                    .body(Map.of("erro", "Aluno nao encontrado."));
        }

        return ResponseEntity.ok(Map.of(
                "identificadorAcademico", aluno.getIdentificadorAcademico(),
                "nome", aluno.getNome(),
                "email", aluno.getEmail()
        ));
    }
}
