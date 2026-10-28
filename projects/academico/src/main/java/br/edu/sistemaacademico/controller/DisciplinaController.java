package br.edu.sistemaacademico.controller;

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

    private final DisciplinaRepository disciplinaRepository;

    public DisciplinaController(
            DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Map<String, Object>> buscarPorCodigo(
            @PathVariable String codigo) {

        return disciplinaRepository.buscarPorCodigo(codigo)
                .map(disciplina -> ResponseEntity.ok(Map.<String, Object>of(
                        "codigo", disciplina.getCodigo(),
                        "nome", disciplina.getNome(),
                        "cargaHoraria", disciplina.getCargaHoraria()
                )))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> cadastrar(
            @RequestBody Map<String, Object> dados) {

        Object codigoValor = dados.get("codigo");
        Object nomeValor = dados.get("nome");
        Object cargaValor = dados.get("cargaHoraria");

        if (!(codigoValor instanceof String codigo)
                || codigo.isBlank()
                || !(nomeValor instanceof String nome)
                || nome.isBlank()
                || cargaValor == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            int cargaHoraria;

            if (cargaValor instanceof Number numero) {
                cargaHoraria = numero.intValue();
            } else {
                return ResponseEntity.badRequest().build();
            }

            Disciplina disciplina =
                    new Disciplina(codigo, nome, cargaHoraria);

            disciplinaRepository.salvar(disciplina);

            return ResponseEntity.status(201).body(Map.of(
                    "codigo", disciplina.getCodigo(),
                    "nome", disciplina.getNome(),
                    "cargaHoraria", disciplina.getCargaHoraria()
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).build();
        }
    }
}
