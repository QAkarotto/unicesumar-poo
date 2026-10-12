package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {
    private final DadosEmMemoria dados;

    public AlunoController(DadosEmMemoria dados) {
        this.dados = dados;
    }

    @GetMapping("/{ra}")
    public Map<String, String> consultar(@PathVariable String ra) {
        Aluno aluno = dados.alunos.get(ra);
        if (aluno == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado");
        }
        return Map.of(
                "ra", ra,
                "nome", aluno.getNome(),
                "email", aluno.getEmail());
    }
}