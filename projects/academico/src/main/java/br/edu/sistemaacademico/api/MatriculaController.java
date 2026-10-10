package br.edu.sistemaacademico.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(List.of(
            Map.of(
                "codigo", "MAT-001",
                "ra", "25131800-2",
                "aluno", "Luiz Henrique de Amorais Franco",
                "turma", "ESOF7AS-NA",
                "disciplina", "POO"
            )
        ));
    }

    @GetMapping("/info")
    public ResponseEntity<?> info() {
        return ResponseEntity.ok(Map.of(
            "ra", "25131800-2",
            "nome", "Luiz Henrique de Amorais Franco",
            "curso", "Engenharia de Software"
        ));
    }
}
