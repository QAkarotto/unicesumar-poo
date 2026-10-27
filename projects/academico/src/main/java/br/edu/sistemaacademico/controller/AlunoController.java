package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.repository.AlunoRepository;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoRepository repository;

    public AlunoController(AlunoRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody AlunoRequest request) {
        if (request == null
                || request.identificadorAcademico() == null || request.identificadorAcademico().isBlank()
                || request.nome() == null || request.nome().isBlank()
                || request.email() == null || request.email().isBlank()) {
            return ResponseEntity.badRequest().body("Informe RA, nome e e-mail.");
        }

        try {
            Aluno aluno = new Aluno(
                    request.identificadorAcademico().trim(),
                    request.nome().trim(),
                    request.email().trim());
            repository.salvar(aluno);
            return ResponseEntity.status(HttpStatus.CREATED).body(aluno);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RegistroDuplicadoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/{ra}")
    public ResponseEntity<Aluno> buscarPorRa(@PathVariable String ra) {
        return repository.buscarPorRa(ra)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
