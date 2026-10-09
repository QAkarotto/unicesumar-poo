package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.dto.AlunoResponse;
import br.edu.sistemaacademico.config.DadosAcademicos;
import br.edu.sistemaacademico.dto.ErroResponse;
import br.edu.sistemaacademico.domain.Aluno;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final DadosAcademicos dados;

    public AlunoController(DadosAcademicos dados) {
        this.dados = dados;
    }

    // GET /alunos/RA2026001
    @GetMapping("/{registroAcademico}")
    public ResponseEntity<?> consultar(@PathVariable("registroAcademico") String registroAcademico) {
        Aluno aluno = dados.buscarAluno(registroAcademico);

        if (aluno == null) {
            return ErroResponse.de(HttpStatus.NOT_FOUND,
                    "Aluno não encontrado: " + registroAcademico); // 404
        }

        return ResponseEntity.ok(AlunoResponse.de(aluno)); // 200
    }
}
