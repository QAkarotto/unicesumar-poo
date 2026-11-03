
package br.edu.sistemaacademico.controller;
import br.edu.sistemaacademico.repository.AlunoRepository;
import br.edu.sistemaacademico.domain.Aluno;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoRepository alunoRepository;

    public AlunoController(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Map<String, String> dados) {
        try {
            String ra = dados.get("identificadorAcademico");
            String nome = dados.get("nome");
            String email = dados.get("email");

            if (alunoRepository.buscarPorRa(ra).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("erro", "Aluno já cadastrado."));
            }

            Aluno aluno = new Aluno(ra, nome, email);
            alunoRepository.salvar(aluno);

            return ResponseEntity.status(HttpStatus.CREATED).body(aluno);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/{ra}")
    public ResponseEntity<?> buscarPorRa(@PathVariable String ra) {
        return alunoRepository.buscarPorRa(ra)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("erro", "Aluno não encontrado.")));
    }
}
