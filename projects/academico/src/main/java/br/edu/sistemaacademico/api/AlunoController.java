package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.Aluno;
import br.edu.sistemaacademico.repository.AlunoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/**
 * Só cuida de HTTP: recebe JSON, chama o domínio/Repository e devolve status + JSON.
 * Não conhece SQL nem JDBC: depende apenas da interface AlunoRepository.
 */
@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoRepository alunoRepository;

    public AlunoController(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody NovoAlunoRequest req) {
        // O construtor de Aluno aplica as regras (campos obrigatórios, e-mail válido).
        // Se algo estiver errado, lança IllegalArgumentException -> 400 (TratamentoErros).
        var aluno = new Aluno(req.identificadorAcademico(), req.nome(), req.email());

        // RA repetido -> RegistroDuplicadoException -> 409 (TratamentoErros)
        alunoRepository.salvar(aluno);

        return ResponseEntity
                .created(URI.create("/api/alunos/" + aluno.getIdentificadorAcademico()))
                .body(AlunoResponse.de(aluno));
    }

    @GetMapping("/{identificador}")
    public ResponseEntity<?> consultar(@PathVariable String identificador) {
        var aluno = alunoRepository.buscarPorRa(identificador);

        if (aluno.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErroResponse("Aluno não encontrado: " + identificador));
        }

        return ResponseEntity.ok(AlunoResponse.de(aluno.get()));
    }

    // JSON recebido no POST
    public record NovoAlunoRequest(String identificadorAcademico, String nome, String email) {}

    // JSON devolvido: só os dados simples (devolver o Aluno direto geraria loop no JSON)
    public record AlunoResponse(String identificadorAcademico, String nome, String email) {

        static AlunoResponse de(Aluno a) {
            return new AlunoResponse(a.getIdentificadorAcademico(), a.getNome(), a.getEmail());
        }
    }
}
