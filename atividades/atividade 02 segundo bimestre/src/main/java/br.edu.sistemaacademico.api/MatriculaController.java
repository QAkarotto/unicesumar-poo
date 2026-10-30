package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.api.Dtos.CriarMatriculaRequest;
import br.edu.sistemaacademico.api.Dtos.MatriculaResponse;
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

    // GET /api/matriculas/MAT-001 -> {id} vira o parâmetro "id" via @PathVariable
    @GetMapping("/{id}")
    public MatriculaResponse consultar(@PathVariable String id) {
        return dados.buscarMatricula(id)
                .map(MatriculaResponse::de)
                .orElseThrow(() -> new ErroApi(HttpStatus.NOT_FOUND, "Matrícula não encontrada: " + id));
    }

    // POST /api/matriculas -> o JSON do corpo vira um CriarMatriculaRequest via @RequestBody
    @PostMapping
    public ResponseEntity<MatriculaResponse> criar(@RequestBody CriarMatriculaRequest requisicao) {
        if (vazio(requisicao.registroAcademico()) || vazio(requisicao.turma()) || vazio(requisicao.disciplina())) {
            throw new ErroApi(HttpStatus.BAD_REQUEST,
                    "Informe registroAcademico, turma e disciplina.");
        }

        var matricula = dados.matricular(
                requisicao.registroAcademico().trim(),
                requisicao.turma().trim(),
                requisicao.disciplina().trim());

        return ResponseEntity
                .created(URI.create("/api/matriculas/" + matricula.getCodigo()))
                .body(MatriculaResponse.de(matricula));
    }

    private static boolean vazio(String valor) {
        return valor == null || valor.isBlank();
    }
}
