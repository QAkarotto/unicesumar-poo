package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    @GetMapping
    public ResponseEntity<?> listar() {
        Aluno aluno = new Aluno("25131800-2", "Luiz Henrique de Amorais Franco", "luiz@unicesumar.edu.br");
        
        return ResponseEntity.ok(List.of(Map.of(
            "ra", "25131800-2",
            "nome", aluno.getNome(),
            "email", "luiz@unicesumar.edu.br"
        )));
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
