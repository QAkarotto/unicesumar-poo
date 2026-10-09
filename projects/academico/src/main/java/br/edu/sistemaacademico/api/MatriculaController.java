package br.edu.sistemaacademico.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {
    private final DadosAcademicos dados;

    public MatriculaController(DadosAcademicos dados) {
        this.dados = dados;
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<MatriculaResposta> buscar(@PathVariable String codigo) {
        return dados.buscarMatricula(codigo)
                .map(MatriculaResposta::de)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MatriculaResposta> criar(@RequestBody NovaMatriculaRequisicao requisicao) {
        var aluno = dados.buscarAluno(requisicao.registroAcademico())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Aluno não encontrado."));
        var oferta = dados.buscarOferta(requisicao.turma(), requisicao.disciplina())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Oferta da disciplina não encontrada nesta turma."));

        // as regras (aluno já matriculado na oferta, aluno já aprovado) ficam no domínio
        var matricula = oferta.matricular(dados.proximoCodigoMatricula(), aluno);

        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{codigo}")
                .buildAndExpand(matricula.getCodigo())
                .toUri();
        return ResponseEntity.created(location).body(MatriculaResposta.de(matricula));
    }
}
