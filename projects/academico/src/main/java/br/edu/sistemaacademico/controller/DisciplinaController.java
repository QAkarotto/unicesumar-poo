
package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.DisciplinaRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    private final DisciplinaRepository repository;

    public DisciplinaController(DisciplinaRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(
            @RequestBody Map<String, Object> dados) {

        Object carga = dados.get("cargaHoraria");

        if (!(carga instanceof Integer cargaHoraria)) {
            throw new IllegalArgumentException(
                    "Carga horária deve ser um número inteiro.");
        }

        Disciplina disciplina = new Disciplina(
                (String) dados.get("codigo"),
                (String) dados.get("nome"),
                cargaHoraria
        );

        repository.salvar(disciplina);

        return ResponseEntity
                .created(URI.create(
                        "/api/disciplinas/" + disciplina.getCodigo()))
                .body(criarResposta(disciplina));
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<?> buscar(
            @PathVariable String codigo) {

        return repository.buscarPorCodigo(codigo)
                .<ResponseEntity<?>>map(disciplina ->
                        ResponseEntity.ok(criarResposta(disciplina)))
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }

    private Map<String, Object> criarResposta(
            Disciplina disciplina) {

        return Map.of(
                "codigo", disciplina.getCodigo(),
                "nome", disciplina.getNome(),
                "cargaHoraria", disciplina.getCargaHoraria()
        );
    }
}
