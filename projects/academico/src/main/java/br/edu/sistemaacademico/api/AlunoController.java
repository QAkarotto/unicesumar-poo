package br.edu.sistemaacademico.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final BaseEmMemoria base;

    public AlunoController(BaseEmMemoria base) {
        this.base = base;
    }

    @GetMapping("/{ra}")
    public ResponseEntity<?> consultar(@PathVariable("ra") String ra) {
        var aluno = base.buscarAluno(ra);
        if (aluno.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(Map.of("erro", "Aluno não encontrado: " + ra));
        }
        return ResponseEntity.ok(AlunoResponse.de(aluno.get()));
    }
}