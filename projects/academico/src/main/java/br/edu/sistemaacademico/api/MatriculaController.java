package br.edu.sistemaacademico.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {
    private final DadosEmMemoria dados;

    public MatriculaController(DadosEmMemoria dados) {
        this.dados = dados;
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<MatriculaResposta> buscar(@PathVariable String codigo) {
        return ResponseEntity.of(
                dados.buscarMatricula(codigo).map(MatriculaResposta::de)
        );
    }

    @PostMapping
    public ResponseEntity<MatriculaResposta> criar(@RequestBody NovaMatricula novaMatricula) {
        var aluno = dados.buscarAluno(novaMatricula.registroAcademico())
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado."));

        var oferta = dados.buscarOferta(novaMatricula.turma(), novaMatricula.disciplina())
                .orElseThrow(() -> new IllegalArgumentException(
                        "A disciplina não é ofertada para esta turma."
                ));

        var matricula = oferta.matricular(dados.proximoCodigo(), aluno);

        return ResponseEntity
                .created(URI.create("/api/matriculas/" + matricula.getCodigo()))
                .body(MatriculaResposta.de(matricula));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> dadosInvalidos(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> operacaoNaoPermitida(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("erro", e.getMessage()));
    }
}
