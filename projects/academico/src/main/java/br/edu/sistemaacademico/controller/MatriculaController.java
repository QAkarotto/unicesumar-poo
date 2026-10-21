
package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.dto.CadastrarMatriculaRequest;
import br.edu.sistemaacademico.repository.DadosAcademicos;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final DadosAcademicos dadosAcademicos;

    public MatriculaController(DadosAcademicos dadosAcademicos) {
        this.dadosAcademicos = dadosAcademicos;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarMatricula(@PathVariable String id) {

        return dadosAcademicos.buscarMatricula(id)
                .<ResponseEntity<?>>map(matricula ->
                        ResponseEntity.ok(criarResposta(matricula))
                )
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> cadastrarMatricula(
            @RequestBody CadastrarMatriculaRequest request
    ) {

        if (request == null
                || request.registroAcademico() == null
                || request.registroAcademico().isBlank()
                || request.codigoOferta() == null
                || request.codigoOferta().isBlank()) {

            return ResponseEntity.badRequest().body(
                    Map.of("erro", "RA e código da oferta são obrigatórios.")
            );
        }

        var aluno = dadosAcademicos
                .buscarAluno(request.registroAcademico());

        if (aluno.isEmpty()) {
            return ResponseEntity.status(404).body(
                    Map.of("erro", "Aluno não encontrado.")
            );
        }

        var oferta = dadosAcademicos
                .buscarOferta(request.codigoOferta());

        if (oferta.isEmpty()) {
            return ResponseEntity.status(404).body(
                    Map.of("erro", "Oferta de disciplina não encontrada.")
            );
        }

        try {
            Matricula matricula = dadosAcademicos.cadastrarMatricula(
                    aluno.get(),
                    oferta.get()
            );

            return ResponseEntity
                    .created(URI.create("/api/matriculas/" + matricula.getCodigo()))
                    .body(criarResposta(matricula));

        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("erro", e.getMessage())
            );
        }
    }

    private Map<String, Object> criarResposta(Matricula matricula) {

        Map<String, Object> resposta = new LinkedHashMap<>();

        resposta.put("codigo", matricula.getCodigo());

        resposta.put(
                "registroAcademico",
                matricula.getAluno().getRegistroAcademico()
        );

        resposta.put(
                "disciplina",
                matricula.getOfertaDisciplina()
                        .getDisciplina().getCodigo()
        );

        resposta.put(
                "turma",
                matricula.getTurma().getCodigo()
        );

        resposta.put("situacao", matricula.getSituacao());
        resposta.put("resultado", matricula.getResultado());

        return resposta;
    }
}
