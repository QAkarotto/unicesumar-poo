package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    @GetMapping("/{ra}")
    public ResponseEntity<Map<String, String>> buscar(@PathVariable String ra) {
        Aluno aluno = MatriculaController.PonteApi.aluno(ra);
        if (aluno == null) {
            return ResponseEntity.status(404).body(Map.of("mensagem", "Aluno não encontrado."));
        }
        return ResponseEntity.ok(Map.of(
                "ra", aluno.getIdentificadorAcademico(),
                "nome", aluno.getNome(),
                "email", aluno.getEmail()));
    }
}
