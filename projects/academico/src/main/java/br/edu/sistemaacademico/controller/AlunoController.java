package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.config.Dados;
import br.edu.sistemaacademico.domain.Aluno;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/alunos")
public class AlunoController {
    private final Dados dados;
    public AlunoController(Dados dados) {
        this.dados = dados;
    }
    @GetMapping
    public ResponseEntity<List<Aluno>> listarTodos() {
        return ResponseEntity.ok(dados.getAlunos());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Aluno> consultarAluno(@PathVariable("id") String id) {
        Aluno aluno = dados.buscarAlunoPorRa(id);

        if (aluno == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(aluno);
    }
}
