package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.ResultadoAcademico;
import br.edu.sistemaacademico.domain.SituacaoMatricula;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final DadosEmMemoria dados;

    public MatriculaController(DadosEmMemoria dados) {
        this.dados = dados;
    }

    /** JSON recebido no POST. A oferta é identificada pela turma + disciplina. */
    public record NovaMatricula(String registroAcademico, String turma, String disciplina) {
    }

    /** JSON devolvido. Resposta própria para não serializar o grafo completo do domínio. */
    public record MatriculaResponse(
            String codigo,
            String registroAcademico,
            String nomeAluno,
            String turma,
            String disciplina,
            SituacaoMatricula situacao,
            ResultadoAcademico resultado
    ) {
        static MatriculaResponse de(Matricula matricula) {
            return new MatriculaResponse(
                    matricula.getCodigo(),
                    matricula.getAluno().getRegistroAcademico(),
                    matricula.getAluno().getNome(),
                    matricula.getTurma().getCodigo(),
                    matricula.getOfertaDisciplina().getDisciplina().getCodigo(),
                    matricula.getSituacao(),
                    matricula.getResultado()
            );
        }
    }

    @GetMapping("/{id}")
    public MatriculaResponse consultar(@PathVariable("id") String id) {
        return dados.buscarMatricula(id)
                .map(MatriculaResponse::de)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Matrícula não encontrada: " + id));
    }

    @PostMapping
    public ResponseEntity<MatriculaResponse> criar(@RequestBody NovaMatricula pedido) {
        if (vazio(pedido.registroAcademico()) || vazio(pedido.turma()) || vazio(pedido.disciplina())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Informe registroAcademico, turma e disciplina.");
        }

        var aluno = dados.buscarAluno(pedido.registroAcademico())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Aluno não encontrado: " + pedido.registroAcademico()));
        var turma = dados.buscarTurma(pedido.turma())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Turma não encontrada: " + pedido.turma()));
        var oferta = turma.getOfertas().stream()
                .filter(o -> o.getDisciplina().getCodigo().equals(pedido.disciplina()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "A disciplina " + pedido.disciplina() + " não é ofertada na turma " + pedido.turma()));

        Matricula matricula;
        try {
            // As regras (aluno já aprovado, matrícula duplicada) são validadas pelo domínio.
            matricula = oferta.matricular(dados.proximoCodigoMatricula(), aluno);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }

        return ResponseEntity
                .created(URI.create("/api/matriculas/" + matricula.getCodigo()))
                .body(MatriculaResponse.de(matricula));
    }

    private static boolean vazio(String valor) {
        return valor == null || valor.isBlank();
    }
}
