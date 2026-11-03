
package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final Map<String, Matricula> matriculas;
    private final Map<String, Aluno> alunos;
    private final OfertaDisciplina ofertaDisciplina;

    public MatriculaController(
            Map<String, Matricula> matriculas,
            Map<String, Aluno> alunos,
            OfertaDisciplina ofertaDisciplina) {
        this.matriculas = matriculas;
        this.alunos = alunos;
        this.ofertaDisciplina = ofertaDisciplina;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> buscarPorId(
            @PathVariable String id) {

        Matricula matricula = matriculas.get(id);

        if (matricula == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> resposta = Map.of(
                "codigo", matricula.getCodigo(),
                "situacao", matricula.getSituacao().name(),
                "aluno", Map.of(
                        "registroAcademico",
                        matricula.getAluno().getRegistroAcademico(),
                        "nome", matricula.getAluno().getNome()
                ),
                "disciplina", Map.of(
                        "codigo",
                        matricula.getOfertaDisciplina()
                                .getDisciplina().getCodigo(),
                        "nome",
                        matricula.getOfertaDisciplina()
                                .getDisciplina().getNome()
                ),
                "turma", matricula.getTurma().getCodigo()
        );

        return ResponseEntity.ok(resposta);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> criar(
            @RequestBody Map<String, String> dados) {

        String codigo = dados.get("codigo");
        String registroAcademico = dados.get("registroAcademico");

        if (codigo == null || registroAcademico == null) {
            return ResponseEntity.badRequest().build();
        }

        if (matriculas.containsKey(codigo)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Aluno aluno = alunos.get(registroAcademico);

        if (aluno == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Matricula matricula = ofertaDisciplina.matricular(
                    codigo,
                    aluno
            );

            matriculas.put(matricula.getCodigo(), matricula);

            Map<String, Object> resposta = Map.of(
                    "codigo", matricula.getCodigo(),
                    "situacao", matricula.getSituacao().name(),
                    "aluno", Map.of(
                            "registroAcademico",
                            aluno.getRegistroAcademico(),
                            "nome", aluno.getNome()
                    ),
                    "disciplina", Map.of(
                            "codigo",
                            ofertaDisciplina.getDisciplina().getCodigo(),
                            "nome",
                            ofertaDisciplina.getDisciplina().getNome()
                    ),
                    "turma", ofertaDisciplina.getTurma().getCodigo()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(resposta);

        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
