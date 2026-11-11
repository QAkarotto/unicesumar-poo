package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.repository.AlunoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoRepository alunoRepository;

    public AlunoController(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    @GetMapping("/{identificador}")
    public ResponseEntity<?> consultarAluno(
            @PathVariable String identificador) {

        return alunoRepository.buscarPorIdentificador(identificador)
                .map(aluno -> ResponseEntity.ok(respostaAluno(aluno)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("erro", "Aluno nao encontrado.")));
    }

    @PostMapping
    public ResponseEntity<?> cadastrarAluno(
            @RequestBody Map<String, String> requisicao) {

        try {
            Aluno aluno = new Aluno(
                    requisicao.get("identificadorAcademico"),
                    requisicao.get("nome"),
                    requisicao.get("email")
            );

            if (alunoRepository.buscarPorIdentificador(aluno.getIdentificadorAcademico()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("erro", "Aluno ja cadastrado."));
            }

            alunoRepository.salvar(aluno);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(respostaAluno(aluno));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", e.getMessage()));
        }
    }

    private Map<String, Object> respostaAluno(Aluno aluno) {
        return Map.of(
                "identificadorAcademico", aluno.getIdentificadorAcademico(),
                "nome", aluno.getNome(),
                "email", aluno.getEmail()
        );
    }
}
