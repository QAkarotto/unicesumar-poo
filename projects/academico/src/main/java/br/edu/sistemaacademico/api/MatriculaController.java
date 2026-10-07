package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Matricula;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final DadosEmMemoria dados;

    public MatriculaController(DadosEmMemoria dados) {
        this.dados = dados;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatriculaResposta> consultar(@PathVariable String id) {
        Matricula matricula = dados.buscarMatricula(id);

        if (matricula == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(MatriculaResposta.de(id, matricula));
    }
}