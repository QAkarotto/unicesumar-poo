package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import org.springframework.context.annotation.Profile;

@Profile("!jdbc")
@RestController
@RequestMapping("/api")
public class AcademicoController {
    private final DadosEmMemoria dados;

    public AcademicoController(DadosEmMemoria dados) {
        this.dados = dados;
    }

    @GetMapping("/alunos/{registroAcademico}")
    public AlunoResponse buscarAluno(@PathVariable("registroAcademico") String registroAcademico) {
        return AlunoResponse.de(dados.buscarAluno(registroAcademico));
    }

    @GetMapping("/matriculas/{codigo}")
    public MatriculaResponse buscarMatricula(@PathVariable("codigo") String codigo) {
        return MatriculaResponse.de(dados.buscarMatricula(codigo));
    }

    @PostMapping("/matriculas")
    public ResponseEntity<MatriculaResponse> matricular(@RequestBody CriarMatriculaRequest request) {
        if (request.registroAcademico() == null || request.registroAcademico().isBlank()
                || request.ofertaId() == null || request.ofertaId().isBlank()) {
            throw new IllegalArgumentException("Informe registroAcademico e ofertaId.");
        }
        var matricula = dados.matricular(request.registroAcademico().trim(), request.ofertaId().trim());
        return ResponseEntity.created(URI.create("/api/matriculas/" + matricula.getCodigo()))
                .body(MatriculaResponse.de(matricula));
    }

    public record CriarMatriculaRequest(String registroAcademico, String ofertaId) {}

    public record AlunoResponse(String registroAcademico, String nome, String email) {
        static AlunoResponse de(Aluno aluno) {
            return new AlunoResponse(aluno.getRegistroAcademico(), aluno.getNome(), aluno.getEmail());
        }
    }

    public record MatriculaResponse(String codigo, AlunoResponse aluno, String codigoDisciplina,
                                    String codigoTurma, int ano, Semestre semestre,
                                    SituacaoMatricula situacao, ResultadoAcademico resultado) {
        static MatriculaResponse de(Matricula matricula) {
            var turma = matricula.getTurma();
            return new MatriculaResponse(matricula.getCodigo(), AlunoResponse.de(matricula.getAluno()),
                    matricula.getOfertaDisciplina().getDisciplina().getCodigo(), turma.getCodigo(),
                    turma.getPeriodoLetivo().getAno(), turma.getPeriodoLetivo().getSemestre(),
                    matricula.getSituacao(), matricula.getResultado());
        }
    }
}
