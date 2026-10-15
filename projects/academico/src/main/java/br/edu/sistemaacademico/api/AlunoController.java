package br.edu.sistemaacademico.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// @RestController: esta classe recebe requisições HTTP e responde em JSON.
// @RequestMapping: todas as rotas daqui começam com /api/alunos.
@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final DadosAcademicos dados;

    public AlunoController(DadosAcademicos dados) {
        this.dados = dados;
    }

    // GET /api/alunos/RA2026001
    // @PathVariable pega o valor que está na URL (o RA).
    @GetMapping("/{ra}")
    public ResponseEntity<?> buscar(@PathVariable("ra") String ra) {
        var encontrado = dados.buscarAluno(ra);

        if (encontrado.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404
        }

        var aluno = encontrado.get();
        // Devolvemos só alguns campos. Se devolvêssemos o Aluno inteiro, o JSON
        // incluiria as matrículas, que apontam de volta para o aluno (loop infinito).
        return ResponseEntity.ok(
                new AlunoResposta(aluno.getRegistroAcademico(), aluno.getNome(), aluno.getEmail())
        ); // 200
    }

    // O Spring transforma este record em JSON automaticamente.
    public record AlunoResposta(String ra, String nome, String email) {
    }
}
