package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.dto.ConcluirMatriculaRequest;
import br.edu.sistemaacademico.dto.CriarMatriculaRequest;
import br.edu.sistemaacademico.dto.MatriculaResponse;
import br.edu.sistemaacademico.service.MatriculaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final MatriculaService service;

    public MatriculaController(MatriculaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<MatriculaResponse> matricular(@RequestBody CriarMatriculaRequest request) {
        MatriculaResponse matricula = service.matricular(request);
        return ResponseEntity.created(URI.create("/api/matriculas/" + matricula.id())).body(matricula);
    }

    @GetMapping
    public List<MatriculaResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public MatriculaResponse buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PatchMapping("/{id}/conclusao")
    public MatriculaResponse concluir(@PathVariable Long id, @RequestBody ConcluirMatriculaRequest request) {
        return service.concluir(id, request);
    }
}
