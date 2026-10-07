package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import br.edu.sistemaacademico.domain.Turma;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final DadosEmMemoria dados;

    public MatriculaController(DadosEmMemoria dados) {
        this.dados = dados;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatriculaResposta> consultar(@PathVariable String id) {
        Matricula matricula = dados.buscarMatricula(id);

        if (matricula == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(MatriculaResposta.de(id, matricula));
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody NovaMatriculaRequisicao requisicao) {
        if (requisicao.ra() == null || requisicao.turma() == null || requisicao.disciplina() == null) {
            return ResponseEntity.badRequest()
                    .body(new ErroResposta("Informe ra, turma e disciplina."));
        }

        Aluno aluno = dados.buscarAluno(requisicao.ra());
        if (aluno == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErroResposta("Aluno não encontrado: " + requisicao.ra()));
        }

        Turma turma = dados.buscarTurma(requisicao.turma());
        if (turma == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErroResposta("Turma não encontrada: " + requisicao.turma()));
        }

        OfertaDisciplina oferta = turma.getOfertas().stream()
                .filter(o -> o.getDisciplina().getCodigo().equals(requisicao.disciplina()))
                .findFirst()
                .orElse(null);
        if (oferta == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErroResposta("Disciplina " + requisicao.disciplina()
                            + " não é ofertada na turma " + requisicao.turma()));
        }

        try {
            Matricula matricula = oferta.matricular(aluno);
            String id = dados.registrar(matricula);
            return ResponseEntity.created(URI.create("/api/matriculas/" + id))
                    .body(MatriculaResposta.de(id, matricula));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErroResposta(e.getMessage()));
        }
    }
}