package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.memoria.PontedaApi;
import br.edu.sistemaacademico.memoria.RecursoNaoEncontradoException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final PontedaApi pontedaApi;

    public MatriculaController(PontedaApi pontedaApi) {
        this.pontedaApi = pontedaApi;
    }

    @GetMapping("/{codigo}")
    public MatriculaResponse consultar(@PathVariable String codigo) {
        return pontedaApi.buscarMatricula(codigo)
                .map(MatriculaResponse::de)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Matrícula não encontrada"));
    }

    @PostMapping
    public ResponseEntity<MatriculaResponse> criar(@RequestBody NovaMatriculaRequest pedido) {
        Matricula matricula = pontedaApi.criarMatricula(
                pedido.codigo(),
                pedido.identificadorAluno(),
                pedido.codigoTurma(),
                pedido.codigoDisciplina());

        return ResponseEntity.created(URI.create("/api/matriculas/" + matricula.getCodigo()))
                .body(MatriculaResponse.de(matricula));
    }
}
