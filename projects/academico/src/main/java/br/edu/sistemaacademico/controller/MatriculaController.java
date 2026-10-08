package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.DadosAcademicos;
import br.edu.sistemaacademico.domain.Matricula;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final DadosAcademicos dados;

    public MatriculaController(DadosAcademicos dados) {
        this.dados = dados;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> consultarMatricula(@PathVariable String id) {
        Matricula matricula = dados.buscarMatricula(id);

        if (matricula == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", "Matricula nao encontrada."));
        }

        return ResponseEntity.ok(respostaMatricula(matricula));
    }

    @PostMapping
    public ResponseEntity<?> criarMatricula(
            @RequestBody Map<String, String> requisicao) {

        try {
            String aluno = requisicao.get("identificadorAluno");
            String oferta = requisicao.get("codigoOferta");

            Matricula matricula = dados.criarMatricula(aluno, oferta);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(respostaMatricula(matricula));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", e.getMessage()));

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("erro", e.getMessage()));
        }
    }

    private Map<String, Object> respostaMatricula(Matricula matricula) {
        Map<String, Object> resposta = new LinkedHashMap<>();

        resposta.put("codigo", matricula.getCodigo());
        resposta.put("identificadorAluno",
                matricula.getAluno().getIdentificadorAcademico());

        String codigoOferta =
                matricula.getOfertaDisciplina().getTurma().getCodigo()
                + ":"
                + matricula.getOfertaDisciplina().getDisciplina().getCodigo();

        resposta.put("codigoOferta", codigoOferta);
        resposta.put("resultado", matricula.getResultado());

        return resposta;
    }
}
