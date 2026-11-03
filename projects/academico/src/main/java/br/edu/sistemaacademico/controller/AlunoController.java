
package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.DadosAcademicos;
import br.edu.sistemaacademico.domain.Aluno;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final DadosAcademicos dadosAcademicos;

    public AlunoController(DadosAcademicos dadosAcademicos) {
        this.dadosAcademicos = dadosAcademicos;
    }

    @GetMapping("/{identificador}")
    public AlunoResponse buscarPorRegistro(@PathVariable String identificador) {
        Aluno aluno = dadosAcademicos.buscarAluno(identificador)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aluno não encontrado."
                ));

        return new AlunoResponse(
                aluno.getRegistroAcademico(),
                aluno.getNome(),
                aluno.getEmail()
        );
    }

    public record AlunoResponse(
            String registroAcademico,
            String nome,
            String email
    ) {}
}
