package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.api.Dtos.AlunoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final DadosAcademicos dados;

    public AlunoController(DadosAcademicos dados) {
        this.dados = dados;
    }

    // GET /api/alunos/RA2026001 (o identificador do projeto é o registro acadêmico)
    @GetMapping("/{registroAcademico}")
    public AlunoResponse consultar(@PathVariable String registroAcademico) {
        return dados.buscarAluno(registroAcademico)
                .map(AlunoResponse::de)
                .orElseThrow(() -> new ErroApi(HttpStatus.NOT_FOUND, "Aluno não encontrado: " + registroAcademico));
    }
}
