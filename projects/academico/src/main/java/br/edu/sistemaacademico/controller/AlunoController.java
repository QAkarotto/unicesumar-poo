
package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.repository.AlunoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoRepository alunoRepository;

    public AlunoController(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    @GetMapping("/{identificador}")
    public ResponseEntity<Map<String, Object>> buscarPorIdentificador(
            @PathVariable String identificador) {

        return alunoRepository.buscarPorRa(identificador)
                .map(aluno -> ResponseEntity.ok(Map.<String, Object>of(
                        "registroAcademico", aluno.getRegistroAcademico(),
                        "nome", aluno.getNome(),
                        "email", aluno.getEmail(),
                        "matriculas", aluno.getMatriculas().stream()
                                .map(matricula -> matricula.getCodigo())
                                .toList()
                )))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> cadastrar(
            @RequestBody Map<String, String> dados) {

        String ra = dados.get("registroAcademico");
        String nome = dados.get("nome");
        String email = dados.get("email");

        if (ra == null || ra.isBlank()
                || nome == null || nome.isBlank()
                || email == null || email.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Aluno aluno = new Aluno(ra, nome, email);
            alunoRepository.salvar(aluno);

            return ResponseEntity.status(201).body(Map.of(
                    "registroAcademico", aluno.getRegistroAcademico(),
                    "nome", aluno.getNome(),
                    "email", aluno.getEmail()
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).build();
        }
    }
}
