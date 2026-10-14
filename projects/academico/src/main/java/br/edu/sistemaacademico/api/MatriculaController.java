package br.edu.sistemaacademico.api;

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

    // GET /api/matriculas/MAT-001 -> 200 com a matrícula, ou 404 se não existir.
    @GetMapping("/{id}")
    public MatriculaResponse consultar(@PathVariable String id) {
        return dados.buscarMatricula(id)
                .map(MatriculaResponse::de)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Matrícula não encontrada: " + id));
    }

    // POST /api/matriculas -> 201 Created, 400/404 para dados inválidos, 409 se o domínio proibir.
    @PostMapping
    public ResponseEntity<MatriculaResponse> criar(@RequestBody MatriculaRequest requisicao) {
        if (vazio(requisicao.registroAcademico())
                || vazio(requisicao.turma())
                || vazio(requisicao.disciplina())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Informe registroAcademico, turma e disciplina.");
        }

        var aluno = dados.buscarAluno(requisicao.registroAcademico())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aluno não encontrado: " + requisicao.registroAcademico()));

        var turma = dados.buscarTurma(requisicao.turma())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Turma não encontrada: " + requisicao.turma()));

        var oferta = turma.getOfertas().stream()
                .filter(o -> o.getDisciplina().getCodigo().equals(requisicao.disciplina()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "A turma " + turma.getCodigo() + " não oferta a disciplina "
                                + requisicao.disciplina()));

        var codigo = vazio(requisicao.codigo()) ? dados.proximoCodigo() : requisicao.codigo().trim();
        if (dados.existeMatricula(codigo)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Já existe uma matrícula com o código " + codigo);
        }

        try {
            // As regras de matrícula continuam no domínio (OfertaDisciplina/Matricula/Aluno).
            var matricula = oferta.matricular(codigo, aluno);
            dados.salvar(matricula);
            return ResponseEntity
                    .created(URI.create("/api/matriculas/" + matricula.getCodigo()))
                    .body(MatriculaResponse.de(matricula));
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    private static boolean vazio(String valor) {
        return valor == null || valor.isBlank();
    }
}
