package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.Matricula;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final DadosEmMemoria dados;

    public MatriculaController(DadosEmMemoria dados) {
        this.dados = dados;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> consultar(@PathVariable String id) {
        var matricula = dados.buscarMatricula(id);

        if (matricula.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErroResponse("Matrícula não encontrada: " + id));
        }

        return ResponseEntity.ok(MatriculaResponse.de(id, matricula.get()));
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody NovaMatriculaRequest req) {

        if (vazio(req.raAluno()) || vazio(req.codigoTurma()) || vazio(req.codigoDisciplina())) {
            return ResponseEntity.badRequest().body(new ErroResponse(
                    "Informe raAluno, codigoTurma e codigoDisciplina."));
        }

        var aluno = dados.buscarAluno(req.raAluno());
        if (aluno.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErroResponse("Aluno não encontrado: " + req.raAluno()));
        }

        var oferta = dados.buscarOferta(req.codigoTurma(), req.codigoDisciplina());
        if (oferta.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErroResponse("Oferta não encontrada: turma "
                            + req.codigoTurma() + ", disciplina " + req.codigoDisciplina()));
        }

        try {
            // As regras de matrícula ficam no domínio, não aqui
            Matricula matricula = oferta.get().matricular(aluno.get());
            String id = dados.registrar(matricula);

            return ResponseEntity
                    .created(URI.create("/api/matriculas/" + id))
                    .body(MatriculaResponse.de(id, matricula));

        } catch (IllegalArgumentException | IllegalStateException e) {
            // O domínio recusou a operação
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErroResponse(e.getMessage()));
        }
    }

    private static boolean vazio(String s) {
        return s == null || s.isBlank();
    }

    // JSON recebido no POST
    public record NovaMatriculaRequest(String raAluno, String codigoTurma, String codigoDisciplina) {}

    // JSON devolvido (evita o loop Matricula -> Aluno -> Matricula)
    public record MatriculaResponse(String id, String raAluno, String nomeAluno,
                                    String codigoDisciplina, String nomeDisciplina,
                                    String codigoTurma, String resultado) {

        static MatriculaResponse de(String id, Matricula m) {
            var oferta = m.getOfertaDisciplina();
            return new MatriculaResponse(
                    id,
                    m.getAluno().getIdentificadorAcademico(),
                    m.getAluno().getNome(),
                    oferta.getDisciplina().getCodigo(),
                    oferta.getDisciplina().getNome(),
                    oferta.getTurma().getCodigo(),
                    m.getResultado() == null ? null : m.getResultado().toString());
        }
    }
}