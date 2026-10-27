package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
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
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final BaseAcademica base;

    public MatriculaController(BaseAcademica base) {
        this.base = base;
    }

    // GET /api/matriculas/MAT2026001 -> 200 com a matrícula, ou 404
    @GetMapping("/{id}")
    public MatriculaResponse consultar(@PathVariable("id") String id) {
        return base.buscarMatricula(id)
                .map(MatriculaResponse::de)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Matrícula não encontrada: " + id
                ));
    }

    // POST /api/matriculas -> 201 criada, 400 JSON incompleto, 404 aluno/oferta
    // inexistente, 422 se o domínio recusar (ver ApiExceptionHandler)
    @PostMapping
    public ResponseEntity<MatriculaResponse> criar(@RequestBody NovaMatriculaRequest dados) {
        String codigo = obrigatorio(dados.codigo(), "codigo");
        String ra = obrigatorio(dados.registroAcademico(), "registroAcademico");
        String codigoTurma = obrigatorio(dados.turma(), "turma");
        String codigoDisciplina = obrigatorio(dados.disciplina(), "disciplina");

        Aluno aluno = base.buscarAluno(ra)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Aluno não encontrado: " + ra));

        OfertaDisciplina oferta = base.buscarOferta(codigoTurma, codigoDisciplina)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Oferta não encontrada: disciplina " + codigoDisciplina
                                + " na turma " + codigoTurma));

        // As regras de matrícula são validadas dentro do domínio.
        Matricula matricula = base.matricular(codigo, aluno, oferta);

        URI local = URI.create("/api/matriculas/" + matricula.getCodigo());
        return ResponseEntity.created(local).body(MatriculaResponse.de(matricula));
    }

    private static String obrigatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "O campo '" + campo + "' é obrigatório.");
        }
        return valor.trim();
    }
}
