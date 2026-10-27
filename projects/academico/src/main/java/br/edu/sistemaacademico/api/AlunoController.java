package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.repository.AlunoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoRepository alunos;

    public AlunoController(AlunoRepository alunos) {
        this.alunos = alunos;
    }

    // POST /api/alunos -> 201 criado, 400 JSON incompleto, 409 RA duplicado, 422 regra do domínio
    @PostMapping
    public ResponseEntity<AlunoResponse> cadastrar(@RequestBody NovoAlunoRequest dados) {
        // O domínio valida as regras (nome, e-mail, RA).
        Aluno aluno = new Aluno(dados.registroAcademico(), dados.nome(), dados.email());
        alunos.salvar(aluno);
        URI local = URI.create("/api/alunos/" + aluno.getRegistroAcademico());
        return ResponseEntity.created(local).body(AlunoResponse.de(aluno));
    }

    // GET /api/alunos/RA2026001 -> 200 com o aluno, ou 404
    @GetMapping("/{registroAcademico}")
    public AlunoResponse consultar(@PathVariable("registroAcademico") String registroAcademico) {
        return alunos.buscarPorRa(registroAcademico)
                .map(AlunoResponse::de)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aluno não encontrado: " + registroAcademico
                ));
    }
}
