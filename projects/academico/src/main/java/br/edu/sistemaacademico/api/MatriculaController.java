package br.edu.sistemaacademico.api;

import org.springframework.http.ResponseEntity;
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

    private final BaseEmMemoria base;

    public MatriculaController(BaseEmMemoria base) {
        this.base = base;
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<?> consultar(@PathVariable("codigo") String codigo) {
        var matricula = base.buscarMatricula(codigo);
        if (matricula.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(Map.of("erro", "Matrícula não encontrada: " + codigo));
        }
        return ResponseEntity.ok(MatriculaResponse.de(matricula.get()));
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody MatriculaRequest pedido) {
        if (vazio(pedido.registroAcademico()) || vazio(pedido.turma()) || vazio(pedido.disciplina())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "Informe registroAcademico, turma e disciplina."));
        }

        var aluno = base.buscarAluno(pedido.registroAcademico());
        if (aluno.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(Map.of("erro", "Aluno não encontrado: " + pedido.registroAcademico()));
        }

        var oferta = base.buscarOferta(pedido.turma(), pedido.disciplina());
        if (oferta.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(Map.of("erro", "Oferta não encontrada: " + pedido.disciplina()
                            + " na turma " + pedido.turma()));
        }

        try {
            var matricula = base.matricular(aluno.get(), oferta.get());
            var local = URI.create("/api/matriculas/" + matricula.getCodigo());
            return ResponseEntity.created(local).body(MatriculaResponse.de(matricula));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(422)
                    .body(Map.of("erro", e.getMessage()));
        }
    }

    private static boolean vazio(String texto) {
        return texto == null || texto.isBlank();
    }
}