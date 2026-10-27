
package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.repository.AlunoRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoRepository repository;

    public AlunoController(AlunoRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(
            @RequestBody Map<String, String> dados) {

        Aluno aluno = new Aluno(
                dados.get("registroAcademico"),
                dados.get("nome"),
                dados.get("email")
        );

        repository.salvar(aluno);

        return ResponseEntity
                .created(URI.create(
                        "/api/alunos/" + aluno.getRegistroAcademico()))
                .body(criarResposta(aluno));
    }

    @GetMapping("/{ra}")
    public ResponseEntity<?> buscar(@PathVariable String ra) {

        return repository.buscarPorRa(ra)
                .<ResponseEntity<?>>map(aluno ->
                        ResponseEntity.ok(criarResposta(aluno)))
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }

    private Map<String, Object> criarResposta(Aluno aluno) {

        return Map.of(
                "registroAcademico", aluno.getRegistroAcademico(),
                "nome", aluno.getNome(),
                "email", aluno.getEmail()
        );
    }
}
