package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.api.Dtos.AlunoResponse;
import br.edu.sistemaacademico.api.Dtos.CriarAlunoRequest;
import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.repository.AlunoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/** Só traduz HTTP <-> domínio/Repository. Nenhum SQL aqui. */
@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoRepository alunos;
    private final DadosAcademicos dados;

    public AlunoController(AlunoRepository alunos, DadosAcademicos dados) {
        this.alunos = alunos;
        this.dados = dados;
    }

    // POST /api/alunos -> 201 | 400 campos ausentes | 409 RA já cadastrado | 422 regra do domínio
    @PostMapping
    public ResponseEntity<AlunoResponse> cadastrar(@RequestBody CriarAlunoRequest requisicao) {
        if (requisicao == null || vazio(requisicao.registroAcademico())
                || vazio(requisicao.nome()) || vazio(requisicao.email())) {
            throw new ErroApi(HttpStatus.BAD_REQUEST, "Informe registroAcademico, nome e email.");
        }

        // o construtor do domínio valida os dados (IllegalArgumentException -> 422)
        var aluno = new Aluno(requisicao.registroAcademico(), requisicao.nome(), requisicao.email());
        var salvo = alunos.salvar(aluno);

        var local = UriComponentsBuilder.fromPath("/api/alunos/{ra}")
                .encode().buildAndExpand(salvo.getRegistroAcademico()).toUri();
        return ResponseEntity.created(local).body(AlunoResponse.de(salvo, List.of()));
    }

    // GET /api/alunos/RA2026001 -> 200 | 404
    @GetMapping("/{registroAcademico}")
    public AlunoResponse consultar(@PathVariable String registroAcademico) {
        return alunos.buscarPorRa(registroAcademico)
                .map(aluno -> AlunoResponse.de(aluno, dados.matriculasDoAluno(registroAcademico)))
                .orElseThrow(() -> new ErroApi(HttpStatus.NOT_FOUND, "Aluno não encontrado: " + registroAcademico));
    }

    private static boolean vazio(String valor) {
        return valor == null || valor.isBlank();
    }
}
