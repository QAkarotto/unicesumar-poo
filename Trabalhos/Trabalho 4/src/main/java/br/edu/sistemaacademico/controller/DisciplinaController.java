package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.dto.DisciplinaRequest;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    private final DisciplinaRepository disciplinaRepository;

    public DisciplinaController(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Disciplina> buscarPorCodigo(@PathVariable String codigo) {
        return disciplinaRepository.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok) // HTTP 200
                .orElse(ResponseEntity.notFound().build()); // HTTP 404
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody DisciplinaRequest request) {
        try {
            Disciplina disciplina = new Disciplina(request.getCodigo(), request.getNome(), request.getCargaHoraria());
            Disciplina salva = disciplinaRepository.salvar(disciplina);

            return ResponseEntity
                    .created(URI.create("/api/disciplinas/" + salva.getCodigo()))
                    .body(salva); // HTTP 201

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // HTTP 400
        } catch (RegistroDuplicadoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); // HTTP 409
        }
    }
}
