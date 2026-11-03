package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.Turma;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final DadosEmMemoria dados;

    public MatriculaController(DadosEmMemoria dados) {
        this.dados = dados;
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<MatriculaResponse> buscar(@PathVariable("codigo") String codigo) {
        return dados.buscarMatricula(codigo)
                .map(matricula -> ResponseEntity.ok(MatriculaResponse.from(matricula)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody CriarMatriculaRequest request) {
        if (request == null || estaVazio(request.registroAcademico()) || estaVazio(request.codigoTurma())) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Aluno> aluno = dados.buscarAluno(request.registroAcademico());
        Optional<Turma> turma = dados.buscarTurma(request.codigoTurma());
        if (aluno.isEmpty() || turma.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // As regras de matrícula estão no domínio (Matricula e Aluno/OfertaDisciplina)
        try {
            Matricula matricula = new Matricula(dados.proximoCodigoMatricula(), aluno.get(), turma.get());
            dados.adicionarMatricula(matricula);
            return ResponseEntity
                    .created(URI.create("/api/matriculas/" + matricula.getCodigo()))
                    .body(MatriculaResponse.from(matricula));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("mensagem", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("mensagem", e.getMessage()));
        }
    }

    private boolean estaVazio(String valor) {
        return valor == null || valor.isBlank();
    }
}