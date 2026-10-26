package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {
    private final DisciplinaRepository disciplinaRepository;

    public DisciplinaController(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    @GetMapping
    public ResponseEntity<List<Disciplina>> listarTodas() {
        return ResponseEntity.ok(disciplinaRepository.listarTodas());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Disciplina> consultarDisciplina(@PathVariable("codigo") String codigo) {
        Disciplina disciplina = disciplinaRepository.buscarDisciplinaPorCodigo(codigo);
        if (disciplina == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(disciplina);
    }

    @PostMapping
    public ResponseEntity<Disciplina> cadastrarDisciplina(@RequestBody DisciplinaRequest request) {
        Disciplina disciplina;
        try {
            disciplina = new Disciplina(request.codigo(), request.nome(), request.cargaHoraria());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        if (disciplinaRepository.buscarDisciplinaPorCodigo(disciplina.getCodigo()) != null) {
            return ResponseEntity.status(409).build();
        }
        disciplinaRepository.salvar(disciplina);
        return ResponseEntity.status(201).body(disciplina);
    }

    public record DisciplinaRequest(String codigo, String nome, int cargaHoraria) {
    }
}