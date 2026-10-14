package br.edu.sistemaacademico.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final DadosEmMemoria dados;

    public AlunoController(DadosEmMemoria dados) {
        this.dados = dados;
    }

    // GET /api/alunos/RA2026001 -> 200 com o aluno, ou 404 se não existir.
    @GetMapping("/{registroAcademico}")
    public AlunoResponse consultar(@PathVariable String registroAcademico) {
        return dados.buscarAluno(registroAcademico)
                .map(AlunoResponse::de)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Aluno não encontrado: " + registroAcademico));
    }
}
