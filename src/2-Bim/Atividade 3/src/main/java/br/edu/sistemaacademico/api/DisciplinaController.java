package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    private final DisciplinaRepository disciplinas;

    public DisciplinaController(DisciplinaRepository disciplinas) {
        this.disciplinas = disciplinas;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> cadastrar(@RequestBody Map<String, Object> pedido) {
        try {
            Disciplina disciplina = new Disciplina(
                    texto(pedido.get("codigo")),
                    texto(pedido.get("nome")),
                    carga(pedido.get("cargaHoraria")));
            disciplinas.salvar(disciplina);
            return ResponseEntity.status(201).body(corpo(disciplina));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(Map.of("mensagem", e.getMessage()));
        }
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Map<String, String>> buscar(@PathVariable String codigo) {
        return disciplinas.buscarPorCodigo(codigo)
                .map(disciplina -> ResponseEntity.ok(corpo(disciplina)))
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("mensagem", "Disciplina não encontrada.")));
    }

    private Map<String, String> corpo(Disciplina disciplina) {
        return Map.of(
                "codigo", disciplina.getCodigo(),
                "nome", disciplina.getNome(),
                "cargaHoraria", String.valueOf(disciplina.getCargaHoraria()));
    }

    private String texto(Object valor) {
        if (valor == null) {
            return null;
        }
        return valor.toString();
    }

    private int carga(Object valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Carga horária deve ser positiva");
        }
        try {
            return Integer.parseInt(valor.toString());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Carga horária deve ser positiva");
        }
    }
}
