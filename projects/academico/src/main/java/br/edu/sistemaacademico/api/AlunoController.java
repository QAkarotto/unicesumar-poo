package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.repository.AlunoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoRepository alunoRepository;

    public AlunoController(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    @GetMapping
    public List<Map<String, String>> listarTodos() {
        return alunoRepository.listarTodos()
                .stream()
                .map(this::converterAluno)
                .toList();
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody NovoAluno requisicao) {
        try {
            Aluno aluno = new Aluno(
                    requisicao.identificadorAcademico(),
                    requisicao.nome(),
                    requisicao.email()
            );

            alunoRepository.salvar(aluno);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(converterAluno(aluno));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", e.getMessage()));

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("erro", "Nao foi possivel cadastrar o aluno. Verifique se o identificador ou email ja existe."));
        }
    }

    private Map<String, String> converterAluno(Aluno aluno) {
        return Map.of(
                "identificadorAcademico", aluno.getIdentificadorAcademico(),
                "nome", aluno.getNome(),
                "email", aluno.getEmail()
        );
    }

    public record NovoAluno(
            String identificadorAcademico,
            String nome,
            String email
    ) {
    }
}
