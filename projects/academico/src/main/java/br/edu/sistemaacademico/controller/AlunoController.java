
package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.domain.Aluno;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final Map<String, Aluno> alunos;

    public AlunoController(Map<String, Aluno> alunos) {
        this.alunos = alunos;
    }

    @GetMapping("/{identificador}")
    public ResponseEntity<Map<String, Object>> buscarPorIdentificador(
            @PathVariable String identificador) {

        Aluno aluno = alunos.get(identificador);

        if (aluno == null) {
            return ResponseEntity.notFound().build();
        }

        List<String> codigosMatriculas = aluno.getMatriculas()
                .stream()
                .map(matricula -> matricula.getCodigo())
                .toList();

        Map<String, Object> resposta = Map.of(
                "registroAcademico", aluno.getRegistroAcademico(),
                "nome", aluno.getNome(),
                "email", aluno.getEmail(),
                "matriculas", codigosMatriculas
        );

        return ResponseEntity.ok(resposta);
    }
}
