package br.edu.sistemaacademico.api;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Matricula;

@RestController
@RequestMapping("/api")
public class SistemaAcademicoController {

    private final DadosEmMemoria dados;

    public SistemaAcademicoController(DadosEmMemoria dados) {
        this.dados = dados;
    }

    @GetMapping("/alunos/{ra}")
    public Map<String, Object> buscarAluno(@PathVariable String ra) {
        Aluno aluno = dados.buscarAluno(ra);
        if (aluno == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado");
        }
        return Map.of(
                "registroAcademico", aluno.getRegistroAcademico(),
                "nome", aluno.getNome(),
                "email", aluno.getEmail());
    }

    @GetMapping("/matriculas/{codigo}")
    public Map<String, Object> buscarMatricula(@PathVariable String codigo) {
        Matricula matricula = dados.buscarMatricula(codigo);
        if (matricula == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Matrícula não encontrada");
        }
        return converter(matricula);
    }

    @PostMapping("/matriculas")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> matricular(@RequestBody Map<String, String> pedido) {
        Aluno aluno = dados.buscarAluno(pedido.get("registroAcademico"));
        if (aluno == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado");
        }
        try {
            return converter(dados.getPoo().matricular(aluno));
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    private Map<String, Object> converter(Matricula matricula) {
        return Map.of(
                "codigo", matricula.getCodigo(),
                "aluno", matricula.getAluno().getNome(),
                "disciplina", matricula.getOfertaDisciplina().getDisciplina().getNome(),
                "situacao", matricula.getSituacao());
    }
}