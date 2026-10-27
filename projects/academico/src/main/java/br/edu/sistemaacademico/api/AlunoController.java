package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.api.repository.AlunoRepository;
import br.edu.sistemaacademico.domain.Aluno;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoRepository alunoRepository;

    public AlunoController(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    @PostMapping
    public ResponseEntity<AlunoResponse> cadastrar(@RequestBody NovoAlunoRequest requisicao) {
        // O construtor do domínio valida os dados (IllegalArgumentException -> 400)
        Aluno aluno = new Aluno(
                requisicao.identificador(),
                requisicao.nome(),
                requisicao.email());

        alunoRepository.salvar(aluno); // duplicado -> RegistroDuplicadoException -> 409

        return ResponseEntity.status(HttpStatus.CREATED).body(converter(aluno));
    }

    @GetMapping("/{identificador}")
    public AlunoResponse buscar(@PathVariable String identificador) {
        Aluno aluno = alunoRepository.buscarPorRa(identificador)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Aluno não encontrado."));

        return converter(aluno);
    }

    private AlunoResponse converter(Aluno aluno) {
        return new AlunoResponse(
                aluno.getIdentificadorAcademico(),
                aluno.getNome(),
                aluno.getEmail());
    }
}