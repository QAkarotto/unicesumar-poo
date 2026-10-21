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

import java.net.URI;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoRepository alunos;

    public AlunoController(AlunoRepository alunos) {
        this.alunos = alunos;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody NovoAlunoRequisicao requisicao) {
        if (vazio(requisicao.ra()) || vazio(requisicao.nome()) || vazio(requisicao.email())) {
            return ResponseEntity.badRequest()
                    .body(new ErroResposta("Informe ra, nome e email."));
        }

        try {
            Aluno aluno = alunos.salvar(
                    new Aluno(requisicao.ra(), requisicao.nome(), requisicao.email()));
            return ResponseEntity
                    .created(URI.create("/api/alunos/" + aluno.getRa()))
                    .body(AlunoResposta.de(aluno));
        } catch (RegistroDuplicadoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErroResposta(e.getMessage()));
        }
    }

    @GetMapping("/{ra}")
    public ResponseEntity<?> consultar(@PathVariable String ra) {
        return alunos.buscarPorRa(ra)
                .<ResponseEntity<?>>map(aluno -> ResponseEntity.ok(AlunoResposta.de(aluno)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErroResposta("Aluno não encontrado: " + ra)));
    }

    private boolean vazio(String texto) {
        return texto == null || texto.isBlank();
    }
}
