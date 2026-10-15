package br.edu.sistemaacademico.api;

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

    // GET /api/matriculas/MAT-001
    @GetMapping("/{id}")
    public ResponseEntity<?> consultar(@PathVariable String id) {
        var matricula = dados.buscarMatricula(id);

        if (matricula == null) {
            return ErroResponse.com(HttpStatus.NOT_FOUND, "Matrícula não encontrada: " + id);
        }
        return ResponseEntity.ok(MatriculaResponse.de(matricula));
    }

    // POST /api/matriculas   (corpo JSON -> NovaMatriculaRequest, via @RequestBody)
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody NovaMatriculaRequest pedido) {
        if (!pedido.estaCompleto()) {
            return ErroResponse.com(HttpStatus.BAD_REQUEST,
                    "Informe registroAcademico, turma e disciplina.");
        }

        var aluno = dados.buscarAluno(pedido.registroAcademico());
        if (aluno == null) {
            return ErroResponse.com(HttpStatus.NOT_FOUND,
                    "Aluno não encontrado: " + pedido.registroAcademico());
        }

        var turma = dados.buscarTurma(pedido.turma());
        if (turma == null) {
            return ErroResponse.com(HttpStatus.NOT_FOUND,
                    "Turma não encontrada: " + pedido.turma());
        }

        var oferta = dados.buscarOferta(turma, pedido.disciplina());
        if (oferta == null) {
            return ErroResponse.com(HttpStatus.NOT_FOUND,
                    "A disciplina " + pedido.disciplina() + " não é ofertada na turma " + pedido.turma());
        }

        try {
            // Quem decide se a matrícula é permitida é o DOMÍNIO, não o Controller.
            var matricula = dados.matricular(aluno, oferta);

            return ResponseEntity
                    .created(URI.create("/api/matriculas/" + matricula.getCodigo()))  // 201 + Location
                    .body(MatriculaResponse.de(matricula));
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Regra do domínio violada (ex.: já matriculado, já aprovado) -> 409 Conflict
            return ErroResponse.com(HttpStatus.CONFLICT, e.getMessage());
        }
    }
}
