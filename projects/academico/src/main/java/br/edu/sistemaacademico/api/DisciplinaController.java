package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Disciplina;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    private final DadosEmMemoria dados;

    public DisciplinaController(DadosEmMemoria dados) {
        this.dados = dados;
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

            if (dados.buscarDisciplina(disciplina.getCodigo()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            dados.adicionarDisciplina(disciplina);
            return ResponseEntity
                    .created(URI.create("/api/disciplinas/" + disciplina.getCodigo()))
                    .body(resposta(disciplina));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<?> buscar(@PathVariable("codigo") String codigo) {
        return dados.buscarDisciplina(codigo)
                .<ResponseEntity<?>>map(disciplina -> ResponseEntity.ok(resposta(disciplina)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private Map<String, Object> resposta(Disciplina disciplina) {
        return Map.of(
                "codigo", disciplina.getCodigo(),
                "nome", disciplina.getNome(),
                "cargaHoraria", disciplina.getCargaHoraria());
    }
}