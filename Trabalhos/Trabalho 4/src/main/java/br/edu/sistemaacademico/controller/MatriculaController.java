package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.domain.DadosEmMemoria;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.dto.MatriculaRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final DadosEmMemoria dadosEmMemoria;

    public MatriculaController(DadosEmMemoria dadosEmMemoria) {
        this.dadosEmMemoria = dadosEmMemoria;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Matricula> buscarPorId(@PathVariable String id) {
        Matricula matricula = dadosEmMemoria.buscarMatriculaPorId(id);

        if (matricula == null) {
            return ResponseEntity.notFound().build(); // HTTP 404
        }

        return ResponseEntity.ok(matricula); // HTTP 200
    }

    @PostMapping
    public ResponseEntity<?> criarMatricula(@RequestBody MatriculaRequest request) {
        try {
            Matricula novaMatricula = dadosEmMemoria.realizarMatricula(
                    request.getRaAluno(),
                    request.getCodigoOferta()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(novaMatricula); // HTTP 201

        } catch (IllegalArgumentException | IllegalStateException e) {
            // Repassa as exceções tratadas pelas regras de negócio do domínio
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // HTTP 400
        }
    }
}