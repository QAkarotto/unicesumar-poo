package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
public class MatriculaController {
    private final Map<String, Aluno> alunos = new ConcurrentHashMap<>();
    private final Map<String, Turma> turmas = new ConcurrentHashMap<>();
    private final Map<String, Matricula> matriculas = new ConcurrentHashMap<>();

    public MatriculaController() {
        Aluno aluno = new Aluno("RA2026001", "Paola Oliveira",
                "paola.oliveira@universidade.edu.br");
        Disciplina disciplina = new Disciplina("POO",
                "Programação Orientada a Objetos", 80);
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);
        Turma turma = new Turma("POO-NA", disciplina, periodo);
        Matricula matricula = new Matricula("MAT-001", aluno, turma);

        alunos.put(aluno.getIdentificadorAcademico(), aluno);
        turmas.put(turma.getCodigo(), turma);
        matriculas.put(matricula.getCodigo(), matricula);
    }

    @GetMapping("/matriculas/{id}")
    public ResponseEntity<?> consultarMatricula(@PathVariable String id) {
        Matricula matricula = matriculas.get(id);
        if (matricula == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", "Matrícula não encontrada."));
        }
        return ResponseEntity.ok(matricula);
    }

    @GetMapping("/alunos/{identificador}")
    public ResponseEntity<?> consultarAluno(@PathVariable String identificador) {
        Aluno aluno = alunos.get(identificador);
        if (aluno == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", "Aluno não encontrado."));
        }
        return ResponseEntity.ok(aluno);
    }

    @PostMapping("/matriculas")
    public ResponseEntity<?> criarMatricula(@RequestBody CriarMatriculaRequest request) {
        if (request == null || vazio(request.getCodigo())
                || vazio(request.getIdentificadorAluno())
                || vazio(request.getCodigoTurma())) {
            return ResponseEntity.badRequest().body(Map.of(
                    "erro", "Informe codigo, identificadorAluno e codigoTurma."));
        }

        if (matriculas.containsKey(request.getCodigo())) {
            return ResponseEntity.badRequest().body(Map.of(
                    "erro", "Já existe matrícula com esse código."));
        }

        Aluno aluno = alunos.get(request.getIdentificadorAluno());
        if (aluno == null) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Aluno informado não existe."));
        }

        Turma turma = turmas.get(request.getCodigoTurma());
        if (turma == null) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Turma informada não existe."));
        }

        try {
            Matricula matricula = new Matricula(request.getCodigo(), aluno, turma);
            matriculas.put(matricula.getCodigo(), matricula);
            return ResponseEntity.status(HttpStatus.CREATED).body(matricula);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    private boolean vazio(String valor) {
        return valor == null || valor.isBlank();
    }
}
