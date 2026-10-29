 package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.service.SistemaAcademicoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    private final SistemaAcademicoService service;

    public DisciplinaController(SistemaAcademicoService service) {
        this.service = service;
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Disciplina> buscarDisciplina(
            @PathVariable String codigo) {

        Disciplina disciplina = service.buscarDisciplina(codigo);

        if (disciplina == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(disciplina);
    }

    @PostMapping
    public ResponseEntity<Disciplina> cadastrarDisciplina(
            @RequestBody Disciplina disciplina) {

        if (service.buscarDisciplina(disciplina.getCodigo()) != null) {
            return ResponseEntity.status(409).build();
        }

        Disciplina disciplinaSalva =
                service.salvarDisciplina(disciplina);

        return ResponseEntity.status(201).body(disciplinaSalva);
    }
}
