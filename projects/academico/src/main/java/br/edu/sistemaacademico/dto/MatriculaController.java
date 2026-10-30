
package br.edu.sistemaacademico.dto;

import br.edu.sistemaacademico.DadosAcademicos;
import br.edu.sistemaacademico.controller.MatriculaResponse;
import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final DadosAcademicos dadosAcademicos;

    public MatriculaController(DadosAcademicos dadosAcademicos) {
        this.dadosAcademicos = dadosAcademicos;
    }

    @GetMapping("/{id}")
    public MatriculaResponse buscarPorId(@PathVariable String id) {

        Matricula matricula = dadosAcademicos.buscarMatricula(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Matrícula não encontrada."
                ));

        return MatriculaResponse.de(matricula);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MatriculaResponse criar(
            @RequestBody NovaMatriculaRequest request) {

        if (request == null
                || request.codigo() == null
                || request.codigo().isBlank()
                || request.registroAcademico() == null
                || request.registroAcademico().isBlank()
                || request.codigoTurma() == null
                || request.codigoTurma().isBlank()
                || request.codigoDisciplina() == null
                || request.codigoDisciplina().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Todos os campos são obrigatórios."
            );
        }

        if (dadosAcademicos.existeMatricula(request.codigo())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe uma matrícula com esse código."
            );
        }

        Aluno aluno = dadosAcademicos
                .buscarAluno(request.registroAcademico())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aluno não encontrado."
                ));

        OfertaDisciplina oferta = dadosAcademicos.buscarOferta(
                        request.codigoTurma(),
                        request.codigoDisciplina())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Oferta da disciplina não encontrada."
                ));

        try {
            Matricula matricula = new Matricula(
                    request.codigo(),
                    aluno,
                    oferta
            );

            dadosAcademicos.adicionarMatricula(matricula);

            return MatriculaResponse.de(matricula);

        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    e.getMessage()
            );
        }
    }
}
