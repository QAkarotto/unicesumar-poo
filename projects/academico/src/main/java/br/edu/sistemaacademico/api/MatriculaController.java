package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Matricula;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final AlunoController alunoController;
    private final List<Matricula> matriculas = new ArrayList<>();

    public MatriculaController(AlunoController alunoController) {
        this.alunoController = alunoController;
        // Matrícula inicial para o aluno Luiz Henrique de Amorais Franco - 25131800-2
        Aluno aluno = alunoController.getRepositorio().get("25131800-2");
        if (aluno != null) {
            matriculas.add(new Matricula(aluno, "2025-1"));
        }
    }

    @GetMapping
    public List<Matricula> listar() {
        return matriculas;
    }

    @GetMapping("/aluno/{registro}")
    public ResponseEntity<List<Matricula>> porAluno(@PathVariable String registro) {
        if (!alunoController.getRepositorio().containsKey(registro)) {
            return ResponseEntity.notFound().build();
        }
        List<Matricula> result = matriculas.stream()
                .filter(m -> m.getAluno().getRegistro().equals(registro))
                .toList();
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Matricula> matricular(@RequestBody Map<String, String> body) {
        String registro = body.get("registro");
        String periodo = body.getOrDefault("periodo", "2025-1");
        Aluno aluno = alunoController.getRepositorio().get(registro);
        if (aluno == null) return ResponseEntity.notFound().build();
        Matricula nova = new Matricula(aluno, periodo);
        matriculas.add(nova);
        return ResponseEntity.ok(nova);
    }
}
