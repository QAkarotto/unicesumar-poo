package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.repository.AlunoRepository;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

/**
 * Recebe e responde as requisições HTTP de alunos.
 * Não contém SQL: o acesso aos dados é delegado ao {@link AlunoRepository}.
 */
@RestController
@RequestMapping("/api/alunos")
public class AlunoController {
    private final AlunoRepository alunos;

    public AlunoController(AlunoRepository alunos) {
        this.alunos = alunos;
    }

    @PostMapping
    public ResponseEntity<AlunoResponse> cadastrar(@RequestBody CadastrarAlunoRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Informe registroAcademico, nome e email.");
        }
        // O construtor do domínio valida os dados (IllegalArgumentException → 400).
        var aluno = new Aluno(request.registroAcademico(), request.nome(), request.email());
        var salvo = alunos.salvar(aluno);
        var location = URI.create("/api/alunos/"
                + UriUtils.encodePathSegment(salvo.getRegistroAcademico(), StandardCharsets.UTF_8));
        return ResponseEntity.created(location).body(AlunoResponse.de(salvo));
    }

    @GetMapping("/{registroAcademico}")
    public AlunoResponse buscar(@PathVariable("registroAcademico") String registroAcademico) {
        return alunos.buscarPorRa(registroAcademico)
                .map(AlunoResponse::de)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado."));
    }

    public record CadastrarAlunoRequest(String registroAcademico, String nome, String email) {}
}
