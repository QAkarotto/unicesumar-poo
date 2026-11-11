package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.dto.AlunoResponse;
import br.edu.sistemaacademico.dto.CriarAlunoRequest;
import br.edu.sistemaacademico.service.AlunoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoService service;

    public AlunoController(AlunoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AlunoResponse> cadastrar(@RequestBody CriarAlunoRequest request) {
        var aluno = service.cadastrar(request);
        return ResponseEntity.created(URI.create("/api/alunos/" + aluno.registroAcademico()))
                .body(aluno);
    }

    @GetMapping
    public ResponseEntity<List<AlunoResponse>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{ra}")
    public ResponseEntity<AlunoResponse> buscarPorRa(@PathVariable("ra") String ra) {
        return ResponseEntity.ok(service.buscarPorRa(ra));
    }
}
