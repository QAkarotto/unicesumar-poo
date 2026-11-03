
package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.dto.DisciplinaRequest;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    private final DisciplinaRepository disciplinaRepository;

    public DisciplinaController(
            DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Disciplina> buscarDisciplina(
            @PathVariable String codigo) {

        return disciplinaRepository.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> cadastrarDisciplina(
            @RequestBody DisciplinaRequest request) {

        try {
            Disciplina disciplina = new Disciplina(
                    request.codigo(),
                    request.nome(),
                    request.cargaHoraria()
            );

            Disciplina salva = disciplinaRepository.salvar(disciplina);

            return ResponseEntity
                    .created(URI.create(
                            "/api/disciplinas/" + salva.getCodigo()))
                    .body(salva);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409)
                    .body(Map.of("erro", e.getMessage()));

        } catch (IllegalStateException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("erro", e.getMessage()));
        }
    }
}
