package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {
    private final DadosEmMemoria dados;

    public MatriculaController(DadosEmMemoria dados) {
        this.dados = dados;
    }

    public record NovaMatricula(String ra, String turma) {}

    @GetMapping("/{id}")
    public Map<String, String> consultar(@PathVariable String id) {
        Matricula matricula = dados.matriculas.get(id);
        if (matricula == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Matrícula não encontrada");
        }
        return resposta(id, matricula);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> criar(@RequestBody NovaMatricula req) {
        Aluno aluno = dados.alunos.get(req.ra());
        Turma turma = dados.turmas.get(req.turma());
        if (aluno == null || turma == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno ou turma não encontrado");
        }
        try {
            Matricula matricula = turma.getOfertas().get(0).matricular(aluno);
            String id = dados.proximoIdMatricula();
            dados.matriculas.put(id, matricula);
            return resposta(id, matricula);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    private Map<String, String> resposta(String id, Matricula matricula) {
        return Map.of(
                "id", id,
                "aluno", matricula.getAluno().getNome(),
                "situacao", String.valueOf(matricula.getSituacao()));
    }
}