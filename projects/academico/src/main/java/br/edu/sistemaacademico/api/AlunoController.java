package br.edu.sistemaacademico.api;

import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{registroAcademico}")
    public ResponseEntity<AlunoResposta> buscar(@PathVariable String registroAcademico) {
        return dados.buscarAluno(registroAcademico)
                .map(AlunoResposta::de)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
