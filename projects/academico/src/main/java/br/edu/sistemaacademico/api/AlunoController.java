package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import java.util.List;
import java.util.Map;
import br.edu.sistemaacademico.repository.AlunoRepository;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@SpringBootApplication(scanBasePackages = "br.edu.sistemaacademico")
@RestController
@RequestMapping("/api/alunos")
public class AlunoController {
    private final AlunoRepository alunoRepository;

    public AlunoController(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(AlunoController.class, args);
    }

    @GetMapping("/{registroAcademico}")
    public ResponseEntity<AlunoResposta> consultar(@PathVariable String registroAcademico) {
        return alunoRepository.buscarPorRegistroAcademico(registroAcademico)
                .map(aluno -> ResponseEntity.ok(AlunoResposta.de(aluno)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody NovoAlunoRequisicao requisicao) {
        try {
            var aluno = alunoRepository.salvar(new Aluno(requisicao.registroAcademico(), requisicao.nome(), requisicao.email()));
            var localizacao = ServletUriComponentsBuilder.fromCurrentRequest().path("/{registroAcademico}")
                    .buildAndExpand(aluno.getRegistroAcademico()).toUri();
            return ResponseEntity.created(localizacao).body(AlunoResposta.de(aluno));
        } catch (RegistroDuplicadoException excecao) {
            return erro(HttpStatus.CONFLICT, excecao.getMessage());
        } catch (IllegalArgumentException excecao) {
            return erro(HttpStatus.UNPROCESSABLE_ENTITY, excecao.getMessage());
        }
    }

    private ResponseEntity<Map<String, String>> erro(HttpStatus status, String mensagem) {
        return ResponseEntity.status(status).body(Map.of("erro", mensagem));
    }

    public record NovoAlunoRequisicao(String registroAcademico, String nome, String email) { }

    public record AlunoResposta(String registroAcademico, String nome, String email, List<String> matriculas) {
        static AlunoResposta de(Aluno aluno) {
            return new AlunoResposta(aluno.getRegistroAcademico(), aluno.getNome(), aluno.getEmail(),
                    aluno.getMatriculas().stream().map(matricula -> matricula.getCodigo()).toList());
        }
    }
}
