package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.Aluno;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final DadosEmMemoria dados;

    public AlunoController(DadosEmMemoria dados) {
        this.dados = dados;
    }

    @GetMapping("/{identificador}")
    public ResponseEntity<?> consultar(@PathVariable String identificador) {
        var aluno = dados.buscarAluno(identificador);

        if (aluno.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new ErroResponse("Aluno não encontrado: " + identificador));
        }

        return ResponseEntity.ok(AlunoResponse.de(aluno.get()));
    }

    // Só os dados simples: devolver o Aluno direto geraria loop no JSON
    public record AlunoResponse(String identificadorAcademico, String nome,
                                String email, int quantidadeMatriculas) {

        static AlunoResponse de(Aluno a) {
            return new AlunoResponse(a.getIdentificadorAcademico(), a.getNome(),
                    a.getEmail(), a.getMatriculas().size());
        }
    }
}