package br.edu.sistemaacademico.api;

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

    private final AlunoRepository alunos;

    public AlunoController(AlunoRepository alunos) {
        this.alunos = alunos;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> cadastrar(@RequestBody Map<String, String> pedido) {
        try {
            Aluno aluno = new Aluno(pedido.get("ra"), pedido.get("nome"), pedido.get("email"));
            alunos.salvar(aluno);
            return ResponseEntity.status(201).body(corpo(aluno));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(Map.of("mensagem", e.getMessage()));
        }
    }

    @GetMapping("/{ra}")
    public ResponseEntity<Map<String, String>> buscar(@PathVariable String ra) {
        return alunos.buscarPorRa(ra)
                .map(aluno -> ResponseEntity.ok(corpo(aluno)))
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("mensagem", "Aluno não encontrado.")));
    }

    private Map<String, String> corpo(Aluno aluno) {
        return Map.of(
                "ra", aluno.getIdentificadorAcademico(),
                "nome", aluno.getNome(),
                "email", aluno.getEmail());
    }
}
