package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.dto.AlunoResponse;
import br.edu.sistemaacademico.dto.CriarAlunoRequest;
import br.edu.sistemaacademico.service.AlunoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        AlunoResponse aluno = service.cadastrar(request);
        return ResponseEntity.created(URI.create("/api/alunos/" + aluno.ra())).body(aluno);
    }

    @GetMapping
    public List<AlunoResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{ra}")
    public AlunoResponse buscar(@PathVariable String ra) {
        return service.buscarPorRa(ra);
    }
}
