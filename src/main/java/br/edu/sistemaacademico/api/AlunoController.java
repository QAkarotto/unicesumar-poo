package br.edu.sistemaacademico.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.repository.AlunoRepository;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoRepository repository;
    private final DadosAcademicos dados;

    public AlunoController(AlunoRepository repository, DadosAcademicos dados) {
        this.repository = repository;
        this.dados = dados;
    }

    @PostMapping
    public ResponseEntity<AlunoResponse> criar(@RequestBody CriarAlunoRequest request) {
        if (request == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Aluno aluno = new Aluno(request.registroAcademico(), request.nome(), request.email());
            repository.salvar(aluno);
            var location = UriComponentsBuilder.fromPath("/api/alunos")
                    .pathSegment(aluno.getRegistroAcademico()).build().encode().toUri();
            return ResponseEntity.created(location).body(AlunoResponse.from(aluno));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{identificador}")
    public ResponseEntity<AlunoResponse> buscar(@PathVariable String identificador) {
        try {
            return ResponseEntity.ok(AlunoResponse.from(dados.buscarAluno(identificador)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
