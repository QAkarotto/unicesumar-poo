package br.edu.sistemaacademico.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final DadosEmMemoria dados;

    // O Spring entrega aqui a instância única de DadosEmMemoria.
    public AlunoController(DadosEmMemoria dados) {
        this.dados = dados;
    }

    // GET /api/alunos/RA2026001  ->  {identificador} chega no parâmetro via @PathVariable
    @GetMapping("/{registroAcademico}")
    public ResponseEntity<?> consultar(@PathVariable String registroAcademico) {
        var aluno = dados.buscarAluno(registroAcademico);

        if (aluno == null) {
            return ErroResponse.com(HttpStatus.NOT_FOUND,
                    "Aluno não encontrado: " + registroAcademico);
        }
        return ResponseEntity.ok(AlunoResponse.de(aluno));   // 200 + JSON
    }
}
