package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Matricula;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class MatriculaController {

private final DadosAcademicos dadosAcademicos;

public MatriculaController(DadosAcademicos dadosAcademicos) {
    this.dadosAcademicos = dadosAcademicos;
}

@GetMapping("/alunos/{identificador}")
public ResponseEntity<?> buscarAluno(@PathVariable String identificador) {
    return dadosAcademicos.buscarAluno(identificador)
            .<ResponseEntity<?>>map(aluno -> ResponseEntity.ok(
                    Map.of(
                            "identificadorAcademico", aluno.getIdentificadorAcademico(),
                            "nome", aluno.getNome(),
                            "email", aluno.getEmail()
                    )
            ))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", "Aluno nÃ£o encontrado.")));
}

@GetMapping("/matriculas/{id}")
public ResponseEntity<?> buscarMatricula(@PathVariable String id) {
    return dadosAcademicos.buscarMatricula(id)
            .<ResponseEntity<?>>map(matricula ->
                    ResponseEntity.ok(convertirMatricula(
                            dadosAcademicos.identificarMatricula(matricula),
                            matricula
                    ))
            )
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", "MatrÃ­cula nÃ£o encontrada.")));
}

@PostMapping("/matriculas")
public ResponseEntity<?> criarMatricula(@RequestBody NovaMatricula requisicao) {
    try {
        Matricula matricula = dadosAcademicos.criarMatricula(
                requisicao.alunoIdentificador(),
                requisicao.turmaCodigo(),
                requisicao.disciplinaCodigo()
        ).orElseThrow();

        String id = dadosAcademicos.identificarMatricula(matricula);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(convertirMatricula(id, matricula));

    } catch (RecursoNaoEncontradoException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("erro", e.getMessage()));

    } catch (IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("erro", e.getMessage()));

    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest()
                .body(Map.of("erro", e.getMessage()));
    }
}

private Map<String, Object> convertirMatricula(String id, Matricula matricula) {
    Aluno aluno = matricula.getAluno();

    return Map.of(
            "id", id,
            "alunoIdentificador", aluno.getIdentificadorAcademico(),
            "alunoNome", aluno.getNome(),
            "turmaCodigo", matricula.getOfertaDisciplina().getTurma().getCodigo(),
            "disciplinaCodigo", matricula.getOfertaDisciplina().getDisciplina().getCodigo(),
            "disciplinaNome", matricula.getOfertaDisciplina().getDisciplina().getNome()
    );
}

public record NovaMatricula(
        String alunoIdentificador,
        String turmaCodigo,
        String disciplinaCodigo
) {}

}
