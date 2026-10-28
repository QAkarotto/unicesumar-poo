package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.repository.AlunoRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoRepository alunos;

    public AlunoController(AlunoRepository alunos) {
        this.alunos = alunos;
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AlunoResponse> cadastrar(
            @RequestBody NovoAlunoRequest request
    ) {
        // O construtor do domínio valida os dados (IllegalArgumentException -> 400).
        Aluno aluno = new Aluno(
                request.registroAcademico(),
                request.nome(),
                request.email()
        );

        alunos.salvar(aluno); // RA repetido -> RegistroDuplicadoException -> 409

        URI local = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{ra}")
                .buildAndExpand(aluno.getRegistroAcademico())
                .toUri();

        return ResponseEntity
                .created(local)
                .body(AlunoResponse.de(aluno));
    }

    @GetMapping(
            value = "/{ra}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> consultar(@PathVariable("ra") String ra) {
        return alunos.buscarPorRa(ra)
                .<ResponseEntity<?>>map(aluno -> ResponseEntity.ok(AlunoResponse.de(aluno)))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(Map.of("erro", "Aluno não encontrado.")));
    }
}
