package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import br.edu.sistemaacademico.memoria.AcademicoMemoria;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AcademicoController {

    private final AcademicoMemoria memoria;

    public AcademicoController(AcademicoMemoria memoria) {
        this.memoria = memoria;
    }

    @GetMapping(
            value = "/alunos/{identificador}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> consultarAluno(
            @PathVariable("identificador") String identificador
    ) {
        Aluno aluno = memoria.buscarAluno(identificador);

        if (aluno == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", "Aluno não encontrado."));
        }

        return ResponseEntity.ok(Map.of(
                "registroAcademico", aluno.getRegistroAcademico(),
                "nome", aluno.getNome(),
                "email", aluno.getEmail()
        ));
    }

    @GetMapping(
            value = "/matriculas/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> consultarMatricula(
            @PathVariable("id") String id
    ) {
        Matricula matricula = memoria.buscarMatricula(id);

        if (matricula == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", "Matrícula não encontrada."));
        }

        return ResponseEntity.ok(representarMatricula(matricula));
    }

    @PostMapping(
            value = "/matriculas",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> criarMatricula(
            @RequestBody NovaMatriculaRequest request
    ) {
        if (request == null
                || vazio(request.codigo())
                || vazio(request.registroAcademico())
                || vazio(request.turma())
                || vazio(request.disciplina())) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("erro", "Todos os campos são obrigatórios."));
        }

        Aluno aluno = memoria.buscarAluno(request.registroAcademico());
        if (aluno == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", "Aluno não encontrado."));
        }

        OfertaDisciplina oferta = memoria.buscarOferta(
                request.turma(),
                request.disciplina()
        );

        if (oferta == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", "Oferta de disciplina não encontrada."));
        }

        try {
            Matricula matricula = oferta.matricular(
                    request.codigo(),
                    aluno
            );

            memoria.registrarMatricula(
                    matricula,
                    request.disciplina()
            );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(representarMatricula(matricula));

        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("erro", e.getMessage()));
        }
    }

    private Map<String, Object> representarMatricula(Matricula matricula) {
        Map<String, Object> aluno = new LinkedHashMap<>();
        aluno.put(
                "registroAcademico",
                matricula.getAluno().getRegistroAcademico()
        );
        aluno.put("nome", matricula.getAluno().getNome());

        Map<String, Object> resposta = new LinkedHashMap<>();
        resposta.put("codigo", matricula.getCodigo());
        resposta.put("aluno", aluno);
        resposta.put("turma", matricula.getTurma().getCodigo());
        resposta.put(
                "disciplina",
                matricula.getOfertaDisciplina().getDisciplina().getCodigo()
        );
        resposta.put("situacao", matricula.getSituacao());
        resposta.put("resultado", matricula.getResultado());

        return resposta;
    }

    private boolean vazio(String valor) {
        return valor == null || valor.isBlank();
    }
}
