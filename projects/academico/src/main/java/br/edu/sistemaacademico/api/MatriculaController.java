package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Matricula;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final DadosAcademicos dados;

    public MatriculaController(DadosAcademicos dados) {
        this.dados = dados;
    }

    // GET /api/matriculas/MAT-001
    @GetMapping("/{codigo}")
    public ResponseEntity<?> buscar(@PathVariable("codigo") String codigo) {
        var encontrada = dados.buscarMatricula(codigo);

        if (encontrada.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404
        }

        return ResponseEntity.ok(resposta(encontrada.get())); // 200
    }

    // POST /api/matriculas
    // @RequestBody: o Spring lê o JSON enviado e monta o objeto NovaMatricula.
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody NovaMatricula pedido) {
        if (pedido.ra() == null || pedido.turma() == null || pedido.disciplina() == null) {
            return ResponseEntity.badRequest() // 400
                    .body(new Erro("Informe ra, turma e disciplina."));
        }

        var aluno = dados.buscarAluno(pedido.ra());
        var oferta = dados.buscarOferta(pedido.turma(), pedido.disciplina());

        if (aluno.isEmpty() || oferta.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND) // 404
                    .body(new Erro("Aluno ou oferta de disciplina não encontrado."));
        }

        try {
            // Quem decide se pode matricular é o domínio (OfertaDisciplina / Matricula / Aluno).
            // O controller não repete nenhuma regra: só chama e trata o erro.
            var matricula = oferta.get().matricular(dados.proximoCodigo(), aluno.get());
            return ResponseEntity.status(HttpStatus.CREATED) // 201
                    .body(resposta(matricula));
        } catch (IllegalArgumentException | IllegalStateException e) {
            // O domínio lançou erro: a regra proíbe esta matrícula.
            return ResponseEntity.status(HttpStatus.CONFLICT) // 409
                    .body(new Erro(e.getMessage()));
        }
    }

    private MatriculaResposta resposta(Matricula matricula) {
        return new MatriculaResposta(
                matricula.getCodigo(),
                matricula.getAluno().getRegistroAcademico(),
                matricula.getAluno().getNome(),
                matricula.getOfertaDisciplina().getDisciplina().getCodigo(),
                matricula.getTurma().getCodigo(),
                matricula.getSituacao().name()
        );
    }

    // JSON que chega no POST: {"ra":"RA2026001","turma":"ESOFT4S-NB","disciplina":"POO"}
    public record NovaMatricula(String ra, String turma, String disciplina) {
    }

    public record MatriculaResposta(String codigo, String ra, String aluno,
                                    String disciplina, String turma, String situacao) {
    }

    public record Erro(String mensagem) {
    }
}
