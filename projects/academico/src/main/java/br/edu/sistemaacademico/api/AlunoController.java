package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Matricula;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final DadosEmMemoria dados;

    public AlunoController(DadosEmMemoria dados) {
        this.dados = dados;
    }

    /**
     * Resposta própria: serializar Aluno direto geraria um ciclo
     * (Aluno -> matrículas -> Matricula -> aluno -> ...), então devolvemos
     * só os dados do aluno e os códigos das suas matrículas.
     */
    public record AlunoResponse(
            String registroAcademico,
            String nome,
            String email,
            List<String> matriculas
    ) {
        static AlunoResponse de(Aluno aluno) {
            return new AlunoResponse(
                    aluno.getRegistroAcademico(),
                    aluno.getNome(),
                    aluno.getEmail(),
                    aluno.getMatriculas().stream().map(Matricula::getCodigo).toList()
            );
        }
    }

    @GetMapping("/{registroAcademico}")
    public AlunoResponse consultar(@PathVariable("registroAcademico") String registroAcademico) {
        return dados.buscarAluno(registroAcademico)
                .map(AlunoResponse::de)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Aluno não encontrado: " + registroAcademico));
    }
}
