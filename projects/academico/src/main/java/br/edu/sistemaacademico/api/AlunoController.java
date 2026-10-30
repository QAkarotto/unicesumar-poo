package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.repository.AlunoRepository;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;
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
    private final DadosEmMemoria dados;

    public AlunoController(AlunoRepository alunos, DadosEmMemoria dados) {
        this.alunos = alunos;
        this.dados = dados;
    }

    // POST /api/alunos -> 201 Created, 400 se os dados forem inválidos, 409 se o RA já existir.
    @PostMapping
    public ResponseEntity<AlunoResponse> cadastrar(@RequestBody AlunoRequest requisicao) {
        Aluno aluno;
        try {
            // O domínio valida os dados (RA, nome e e-mail).
            aluno = new Aluno(requisicao.registroAcademico(), requisicao.nome(), requisicao.email());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        try {
            alunos.salvar(aluno);
        } catch (RegistroDuplicadoException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }

        return ResponseEntity
                .created(URI.create("/api/alunos/" + aluno.getRegistroAcademico()))
                .body(AlunoResponse.de(aluno, dados.matriculasDoAluno(aluno.getRegistroAcademico())));
    }

    // GET /api/alunos/RA2026001 -> 200 com o aluno, ou 404 se não existir.
    @GetMapping("/{registroAcademico}")
    public AlunoResponse consultar(@PathVariable String registroAcademico) {
        return alunos.buscarPorRa(registroAcademico)
                .map(aluno -> AlunoResponse.de(aluno, dados.matriculasDoAluno(registroAcademico)))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Aluno não encontrado: " + registroAcademico));
    }
}
