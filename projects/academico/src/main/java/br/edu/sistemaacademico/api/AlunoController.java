package br.edu.sistemaacademico.api;

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

    @GetMapping("/{registroAcademico}")
    public ResponseEntity<AlunoResposta> buscar(@PathVariable String registroAcademico) {
        return ResponseEntity.of(
                dados.buscarAluno(registroAcademico).map(AlunoResposta::de)
        );
    }
}
