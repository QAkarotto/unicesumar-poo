package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.repository.AlunoRepository;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoRepository alunos;

    public AlunoController(AlunoRepository alunos) {
        this.alunos = alunos;
    }

    public record CriarAlunoRequest(String ra, String nome, String email) {}

    @PostMapping
    public ResponseEntity<AlunoResponse> criar(@RequestBody CriarAlunoRequest request) {
        try {
            Aluno aluno = new Aluno(request.ra(), request.nome(), request.email());

            if (alunos.buscarPorRa(aluno.getRegistroAcademico()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            alunos.salvar(aluno);
            return ResponseEntity
                    .created(URI.create("/api/alunos/" + aluno.getRegistroAcademico()))
                    .body(AlunoResponse.from(aluno));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RegistroDuplicadoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/{ra}")
    public ResponseEntity<AlunoResponse> buscar(@PathVariable("ra") String ra) {
        return alunos.buscarPorRa(ra)
                .map(aluno -> ResponseEntity.ok(AlunoResponse.from(aluno)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}