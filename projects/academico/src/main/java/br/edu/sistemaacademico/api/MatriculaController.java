package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Matricula;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {
    @GetMapping("/{codigo}")
    public ResponseEntity<MatriculaResposta> consultar(@PathVariable String codigo) {
        var matricula = localizarMatricula(codigo);
        return matricula == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(MatriculaResposta.de(matricula));
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody NovaMatriculaRequisicao requisicao) {
        var aluno = AlunoController.obterAluno(requisicao.registroAcademico());
        if (aluno == null) {
            return erro(HttpStatus.NOT_FOUND, "Aluno não encontrado.");
        }
        var oferta = AlunoController.obterOferta(requisicao.codigoTurma(), requisicao.codigoDisciplina());
        if (oferta == null) {
            return erro(HttpStatus.NOT_FOUND, "Oferta de disciplina não encontrada.");
        }
        if (localizarMatricula(requisicao.codigo()) != null) {
            return erro(HttpStatus.UNPROCESSABLE_ENTITY, "Já existe uma matrícula com este código.");
        }

        try {
            Matricula matricula = oferta.matricular(requisicao.codigo(), aluno);
            var localizacao = ServletUriComponentsBuilder.fromCurrentRequest().path("/{codigo}")
                    .buildAndExpand(matricula.getCodigo()).toUri();
            return ResponseEntity.created(localizacao).body(MatriculaResposta.de(matricula));
        } catch (IllegalArgumentException | IllegalStateException excecao) {
            return erro(HttpStatus.UNPROCESSABLE_ENTITY, excecao.getMessage());
        }
    }

    private Matricula localizarMatricula(String codigo) {
        // Todas as matrículas pertencem a uma oferta; a busca usa as coleções do domínio.
        return AlunoController.obterOfertas().stream()
                .flatMap(oferta -> oferta.getMatriculas().stream())
                .filter(matricula -> matricula.getCodigo().equals(codigo))
                .findFirst()
                .orElse(null);
    }

    private ResponseEntity<Map<String, String>> erro(HttpStatus status, String mensagem) {
        return ResponseEntity.status(status).body(Map.of("erro", mensagem));
    }

    public record NovaMatriculaRequisicao(String codigo, String registroAcademico, String codigoTurma,
                                          String codigoDisciplina) { }

    public record MatriculaResposta(String codigo, String registroAcademico, String nomeAluno,
                                    String codigoTurma, String codigoDisciplina, String situacao, String resultado) {
        static MatriculaResposta de(Matricula matricula) {
            return new MatriculaResposta(matricula.getCodigo(), matricula.getAluno().getRegistroAcademico(),
                    matricula.getAluno().getNome(), matricula.getTurma().getCodigo(),
                    matricula.getOfertaDisciplina().getDisciplina().getCodigo(), matricula.getSituacao().name(),
                    matricula.getResultado() == null ? null : matricula.getResultado().name());
        }
    }
}
