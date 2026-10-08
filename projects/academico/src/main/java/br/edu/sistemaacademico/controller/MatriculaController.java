package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.config.Dados;
import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import br.edu.sistemaacademico.domain.Turma;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {
    private final Dados dados;

    public MatriculaController(Dados dados) {
        this.dados = dados;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Matricula> consultarMatricula(@PathVariable("id") String id) {
        Matricula matricula = dados.buscarMatriculaPorCodigo(id);

        if (matricula == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(matricula);
    }

    private OfertaDisciplina buscarOferta(String codigoTurma, String codigoDisciplina) {
        Turma turma = dados.buscarTurmaPorCodigo(codigoTurma);
        if (turma == null) {
            throw new IllegalArgumentException("Turma não encontrada: " + codigoTurma);
        }
        for (OfertaDisciplina oferta : turma.getOfertas()) {
            if (oferta.getDisciplina().getCodigo().equalsIgnoreCase(codigoDisciplina)) {
                return oferta;
            }
        }
        throw new IllegalArgumentException(
                "Disciplina " + codigoDisciplina + " não ofertada na turma " + codigoTurma);
    }

    @PostMapping
    public ResponseEntity<Object> criarMatricula(@RequestBody Map<String, String> request) {
        try {
            Aluno aluno = dados.buscarAlunoPorRa(request.get("raAluno"));
            OfertaDisciplina oferta = buscarOferta(
                    request.get("codigoTurma"),
                    request.get("codigoDisciplina"));

            Matricula matricula = oferta.matricular(aluno);
            dados.salvarMatricula(matricula);

            URI location = URI.create("/api/matriculas/" + matricula.getCodigo());
            return ResponseEntity.created(location).body(matricula);

        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }
}