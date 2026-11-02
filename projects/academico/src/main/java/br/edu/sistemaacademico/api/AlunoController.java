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

    private final DadosEmMemoria dados;

    public AlunoController(DadosEmMemoria dados) {
        this.dados = dados;
    }

    @GetMapping("/{ra}")
    public ResponseEntity<AlunoResponse> buscar(@PathVariable("ra") String ra) {
        return dados.buscarAluno(ra)
                .map(aluno -> ResponseEntity.ok(AlunoResponse.from(aluno)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}