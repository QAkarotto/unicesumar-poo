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
    public ResponseEntity<?> buscarPorId(@PathVariable String id) {
        Matricula matricula = dadosEmMemoria.buscarMatriculaPorId(id);

        if (matricula == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)  // ERRO
                    .body("Matrícula não encontrada!!");
        }

        return ResponseEntity.ok(matricula); //  OK
    }

    @PostMapping
    public ResponseEntity<?> criarMatricula(@RequestBody MatriculaRequest request) {
        try {
            Matricula novaMatricula = dadosEmMemoria.realizarMatricula(
                    request.getRaAluno(),
                    request.getCodigoOferta()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(novaMatricula); // CREATED

        } catch (IllegalArgumentException | IllegalStateException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // ERRO
        }
    }
}