package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;
import java.net.URI;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    private final DisciplinaRepository disciplinas;

    public DisciplinaController(DisciplinaRepository disciplinas) {
        this.disciplinas = disciplinas;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Map<String, Object> corpo) {
        Object carga = corpo.get("cargaHoraria");
        if (!(carga instanceof Integer cargaHoraria)) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Disciplina disciplina = new Disciplina(
                    (String) corpo.get("codigo"),
                    (String) corpo.get("nome"),
                    cargaHoraria);

            if (disciplinas.buscarPorCodigo(disciplina.getCodigo()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            disciplinas.salvar(disciplina);
            return ResponseEntity
                    .created(URI.create("/api/disciplinas/" + disciplina.getCodigo()))
                    .body(resposta(disciplina));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RegistroDuplicadoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<?> buscar(@PathVariable("codigo") String codigo) {
        return disciplinas.buscarPorCodigo(codigo)
                .<ResponseEntity<?>>map(d -> ResponseEntity.ok(resposta(d)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private Map<String, Object> resposta(Disciplina d) {
        return Map.of(
                "codigo", d.getCodigo(),
                "nome", d.getNome(),
                "cargaHoraria", d.getCargaHoraria());
    }
}