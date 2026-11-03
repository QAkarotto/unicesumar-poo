
package br.edu.sistemaacademico.controller;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import br.edu.sistemaacademico.domain.Disciplina;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/disciplinas")
public class DisciplinaController {

    private final DisciplinaRepository disciplinaRepository;

    public DisciplinaController(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Map<String, Object> dados) {
        try {
            String codigo = (String) dados.get("codigo");

            if (disciplinaRepository.buscarPorCodigo(codigo).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("erro", "Disciplina já cadastrada."));
            }

            String nome = (String) dados.get("nome");
            int cargaHoraria = ((Number) dados.get("cargaHoraria")).intValue();

            Disciplina disciplina = new Disciplina(codigo, nome, cargaHoraria);
            disciplinaRepository.salvar(disciplina);

            return ResponseEntity.status(HttpStatus.CREATED).body(disciplina);

        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "Dados inválidos ou incompletos."));
        }
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<?> buscarPorCodigo(@PathVariable String codigo) {
        return disciplinaRepository.buscarPorCodigo(codigo)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("erro", "Disciplina não encontrada.")));
    }
}
