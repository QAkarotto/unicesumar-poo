package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Matricula;
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
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final DadosEmMemoria dados;

    public MatriculaController(DadosEmMemoria dados) {
        this.dados = dados;
    }

    @GetMapping("/{id}")
    public MatriculaResponse buscar(@PathVariable String id) {
        Matricula matricula = dados.buscarMatricula(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Matrícula não encontrada."));

        return converter(matricula);
    }

    @PostMapping
    public ResponseEntity<MatriculaResponse> criar(@RequestBody NovaMatriculaRequest requisicao) {
        dados.buscarAluno(requisicao.identificadorAluno())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Aluno não encontrado."));

        dados.buscarOferta(requisicao.codigoDisciplina())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Disciplina não encontrada."));

        Matricula matricula = dados.matricular(
                requisicao.identificadorAluno(),
                requisicao.codigoDisciplina());

        return ResponseEntity.status(HttpStatus.CREATED).body(converter(matricula));
    }

    private MatriculaResponse converter(Matricula matricula) {
        String resultado = matricula.getResultado() == null
                ? null
                : matricula.getResultado().name();

        return new MatriculaResponse(
                matricula.getId(),
                matricula.getAluno().getIdentificadorAcademico(),
                matricula.getAluno().getNome(),
                matricula.getDisciplina().getNome(),
                resultado);
    }
}