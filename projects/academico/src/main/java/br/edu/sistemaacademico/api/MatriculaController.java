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

    private final CadastroAcademico cadastro;

    public MatriculaController(CadastroAcademico cadastro) {
        this.cadastro = cadastro;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatriculaResposta> consultar(@PathVariable("id") String id) {
        Matricula matricula = cadastro.buscarMatricula(id);

        if (matricula == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(MatriculaResposta.de(matricula));
    }
}