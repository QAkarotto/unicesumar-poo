package br.edu.sistemaacademico.api;

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

    private final DadosEmMemoria dados;

    public AlunoController(DadosEmMemoria dados) {
        this.dados = dados;
    }

    @GetMapping("/{identificador}")
    public AlunoResponse buscar(@PathVariable String identificador) {
        Aluno aluno = dados.buscarAluno(identificador)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Aluno não encontrado."));

        return new AlunoResponse(
                aluno.getIdentificadorAcademico(),
                aluno.getNome(),
                aluno.getEmail());
    }
}