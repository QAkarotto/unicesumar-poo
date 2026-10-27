package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    private final DisciplinaRepository repository;

    public DisciplinaController(DisciplinaRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody DisciplinaRequest request) {
        if (request == null
                || request.codigo() == null || request.codigo().isBlank()
                || request.nome() == null || request.nome().isBlank()
                || request.cargaHoraria() == null) {
            return ResponseEntity.badRequest().body("Informe código, nome e carga horária.");
        }

        try {
            Disciplina disciplina = new Disciplina(
                    request.codigo().trim(),
                    request.nome().trim(),
                    request.cargaHoraria());
            repository.salvar(disciplina);
            return ResponseEntity.status(HttpStatus.CREATED).body(disciplina);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RegistroDuplicadoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Disciplina> buscarPorCodigo(@PathVariable String codigo) {
        return repository.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
