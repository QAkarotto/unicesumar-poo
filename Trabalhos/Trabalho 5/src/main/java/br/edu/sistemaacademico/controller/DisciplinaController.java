package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.dto.CriarDisciplinaRequest;
import br.edu.sistemaacademico.dto.DisciplinaResponse;
import br.edu.sistemaacademico.service.DisciplinaService;
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
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    private final DisciplinaService service;

    public DisciplinaController(DisciplinaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DisciplinaResponse> cadastrar(@RequestBody CriarDisciplinaRequest request) {
        DisciplinaResponse disciplina = service.cadastrar(request);
        return ResponseEntity.created(URI.create("/api/disciplinas/" + disciplina.codigo())).body(disciplina);
    }

    @GetMapping
    public List<DisciplinaResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{codigo}")
    public DisciplinaResponse buscar(@PathVariable String codigo) {
        return service.buscarPorCodigo(codigo);
    }
}
