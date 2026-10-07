package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
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

    private final DadosAcademicos dados;

    public MatriculaController(DadosAcademicos dados) {
        this.dados = dados;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatriculaResponse> buscar(@PathVariable String id) {
        try {
            return ResponseEntity.ok(MatriculaResponse.from(dados.buscarMatricula(id)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<MatriculaResponse> criar(@RequestBody CriarMatriculaRequest request) {
        if (requisicaoIncompleta(request)) {
            return ResponseEntity.badRequest().build();
        }

        Aluno aluno;
        OfertaDisciplina oferta;
        try {
            aluno = dados.buscarAluno(request.registroAcademico());
            oferta = dados.buscarOferta(request.codigoTurma(), request.codigoDisciplina());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }

        try {
            Matricula matricula = oferta.matricular(aluno);
            return ResponseEntity
                    .created(URI.create("/api/matriculas/" + matricula.getCodigo()))
                    .body(MatriculaResponse.from(matricula));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    private boolean requisicaoIncompleta(CriarMatriculaRequest request) {
        return request == null
                || estaVazio(request.registroAcademico())
                || estaVazio(request.codigoTurma())
                || estaVazio(request.codigoDisciplina());
    }

    private boolean estaVazio(String valor) {
        return valor == null || valor.isBlank();
    }
}