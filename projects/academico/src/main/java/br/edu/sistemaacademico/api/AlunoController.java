package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {
    private final Map<String, Aluno> alunos = new HashMap<>();
    public AlunoController() {
        alunos.put("25131800-2", new Aluno("25131800-2", "Luiz Henrique de Amorais Franco", "25131800-2@unicesumar.edu.br"));
        alunos.put("99999999-9", new Aluno("99999999-9", "Aluno Teste", "teste@unicesumar.edu.br"));
    }
    @GetMapping("/{registro}")
    public ResponseEntity<Aluno> buscar(@PathVariable String registro) {
        Aluno a = alunos.get(registro);
        if (a == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(a);
    }
    public Map<String, Aluno> getRepositorio() { return alunos; }
}
