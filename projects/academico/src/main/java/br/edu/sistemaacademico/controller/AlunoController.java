package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.repository.AlunoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {
    private final AlunoRepository alunoRepository;
    public AlunoController(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }
    @GetMapping
    public ResponseEntity<List<Aluno>> listarTodos() {
        return ResponseEntity.ok(alunoRepository.listarTodos());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Aluno> consultarAluno(@PathVariable("id") String id) {
        Aluno aluno = alunoRepository.buscarAlunoPorRa(id);

        if (aluno == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(aluno);
    }
    @PostMapping
    public ResponseEntity<Aluno> cadastrarAluno(@RequestBody AlunoRequest request) {
        Aluno aluno;
        try {
            aluno = new Aluno(request.ra(), request.nome(), request.email());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        if (alunoRepository.buscarAlunoPorRa(aluno.getRa()) != null) {
            return ResponseEntity.status(409).build();
        }
        alunoRepository.salvar(aluno);
        return ResponseEntity.status(201).body(aluno);
    }
    // guarda os dados do JSON
    public record AlunoRequest(String ra, String nome, String email) {
    }

}