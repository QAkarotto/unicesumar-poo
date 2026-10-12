package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final CadastroAcademico cadastro;

    public AlunoController(CadastroAcademico cadastro) {
        this.cadastro = cadastro;
    }

    @GetMapping("/{registroAcademico}")
    public ResponseEntity<AlunoResposta> consultar(@PathVariable("registroAcademico") String registroAcademico) {
        Aluno aluno = cadastro.buscarAluno(registroAcademico);

        if (aluno == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(AlunoResposta.de(aluno));
    }
}