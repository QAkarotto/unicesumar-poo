
package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import br.edu.sistemaacademico.repository.MemoriaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final MemoriaRepository repository;

    public MatriculaController(MemoriaRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Matricula> buscarPorId(@PathVariable String id) {
        return repository.buscarMatriculaPorCodigo(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> matricular(
            @RequestBody MatriculaRequest request) {

        if (request == null
                || request.identificadorAluno() == null
                || request.identificadorAluno().isBlank()
                || request.codigoOferta() == null
                || request.codigoOferta().isBlank()) {

            return ResponseEntity.badRequest()
                    .body("Informe o aluno e a oferta.");
        }

        Aluno aluno = repository
                .buscarAlunoPorIdentificador(request.identificadorAluno())
                .orElse(null);

        if (aluno == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Aluno não encontrado.");
        }

        OfertaDisciplina oferta = repository
                .buscarOfertaPorCodigo(request.codigoOferta())
                .orElse(null);

        if (oferta == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Oferta não encontrada.");
        }

        try {
            Matricula matricula = oferta.matricular(aluno);
            repository.salvarMatricula(matricula);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(matricula);

        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }
}
