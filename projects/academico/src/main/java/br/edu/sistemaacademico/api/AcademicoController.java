package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.api.dto.AlunoResponse;
import br.edu.sistemaacademico.api.dto.CriarMatriculaRequest;
import br.edu.sistemaacademico.api.dto.MatriculaResponse;
import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import br.edu.sistemaacademico.domain.Turma;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class AcademicoController {

    private final DadosAcademicos dados;

    public AcademicoController(DadosAcademicos dados) {
        this.dados = dados;
    }

    @GetMapping("/matriculas/{id}")
    public MatriculaResponse buscarMatricula(@PathVariable String id) {
        Matricula matricula = dados.matriculas.get(id);

        if (matricula == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Matrícula não encontrada");
        }

        return MatriculaResponse.de(matricula);
    }

    @GetMapping("/alunos/{identificador}")
    public AlunoResponse buscarAluno(@PathVariable String identificador) {
        Aluno aluno = dados.alunos.get(identificador);

        if (aluno == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Aluno não encontrado");
        }

        return AlunoResponse.de(aluno);
    }

    @PostMapping("/matriculas")
    public ResponseEntity<MatriculaResponse> criarMatricula(
            @RequestBody CriarMatriculaRequest request) {

        if (request == null
                || vazio(request.codigo())
                || vazio(request.registroAcademico())
                || vazio(request.codigoTurma())
                || vazio(request.codigoDisciplina())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Informe código da matrícula, RA, turma e disciplina");
        }

        if (dados.matriculas.containsKey(request.codigo())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Código de matrícula já existente");
        }

        Aluno aluno = dados.alunos.get(request.registroAcademico());
        if (aluno == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Aluno não encontrado");
        }

        Turma turma = dados.turmas.get(request.codigoTurma());
        if (turma == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Turma não encontrada");
        }

        OfertaDisciplina oferta = turma.getOfertas().stream()
                .filter(o -> o.getDisciplina().getCodigo()
                        .equals(request.codigoDisciplina()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Disciplina não ofertada nessa turma"));

        try {
            Matricula matricula = oferta.matricular(request.codigo(), aluno);
            dados.matriculas.put(matricula.getCodigo(), matricula);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(MatriculaResponse.de(matricula));
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage());
        }
    }

    private boolean vazio(String valor) {
        return valor == null || valor.isBlank();
    }
}