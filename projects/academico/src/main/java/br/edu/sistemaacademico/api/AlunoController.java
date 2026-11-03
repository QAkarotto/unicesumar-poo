package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.persistencia.AlunoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {
    private final AlunoRepository alunos;

    public AlunoController(AlunoRepository alunos) {
        this.alunos = alunos;
    }

    @PostMapping
    public ResponseEntity<AlunoResponse> cadastrar(@RequestBody CriarAlunoRequest request) {
        var aluno = alunos.salvar(new Aluno(request.registroAcademico(), request.nome(), request.email()));
        var location = UriComponentsBuilder.fromPath("/api/alunos/{ra}")
                .buildAndExpand(aluno.getRegistroAcademico()).encode().toUri();
        return ResponseEntity.created(location).body(AlunoResponse.de(aluno));
    }

    @GetMapping("/{registroAcademico}")
    public AlunoResponse buscar(@PathVariable("registroAcademico") String registroAcademico) {
        return alunos.buscarPorRa(registroAcademico)
                .map(AlunoResponse::de)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado."));
    }

    public record CriarAlunoRequest(String registroAcademico, String nome, String email) {}

    public record AlunoResponse(String registroAcademico, String nome, String email) {
        static AlunoResponse de(Aluno aluno) {
            return new AlunoResponse(aluno.getRegistroAcademico(), aluno.getNome(), aluno.getEmail());
        }
    }
}
