package br.edu.sistemaacademico;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.repository.AlunoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoRepository alunoRepository;

    public AlunoController(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    @GetMapping("/{identificador}")
    public AlunoResponse buscarPorRegistro(@PathVariable String identificador) {
        Aluno aluno = alunoRepository.buscarPorRa(identificador)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aluno não encontrado."
                ));

        return new AlunoResponse(
                aluno.getRegistroAcademico(),
                aluno.getNome(),
                aluno.getEmail()
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AlunoResponse criar(@RequestBody NovoAlunoRequest request) {
        if (request == null
                || request.registroAcademico() == null || request.registroAcademico().isBlank()
                || request.nome() == null || request.nome().isBlank()
                || request.email() == null || request.email().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Todos os campos do aluno são obrigatórios."
            );
        }

        if (alunoRepository.buscarPorRa(request.registroAcademico()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe um aluno cadastrado com este RA."
            );
        }

        try {
            Aluno aluno = new Aluno(
                    request.registroAcademico(),
                    request.nome(),
                    request.email()
            );

            alunoRepository.salvar(aluno);

            return new AlunoResponse(
                    aluno.getRegistroAcademico(),
                    aluno.getNome(),
                    aluno.getEmail()
            );

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
        }
    }

    public record AlunoResponse(
            String registroAcademico,
            String nome,
            String email
    ) {}

    public record NovoAlunoRequest(
            String registroAcademico,
            String nome,
            String email
    ) {}
}