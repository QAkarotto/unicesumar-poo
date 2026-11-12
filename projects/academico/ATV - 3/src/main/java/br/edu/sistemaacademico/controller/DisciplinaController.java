package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    private final DisciplinaRepository disciplinaRepository;

    public DisciplinaController(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<?> consultarDisciplina(
            @PathVariable String codigo) {

        return disciplinaRepository.buscarPorCodigo(codigo)
                .map(disciplina -> ResponseEntity.ok(respostaDisciplina(disciplina)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("erro", "Disciplina nao encontrada.")));
    }

    @PostMapping
    public ResponseEntity<?> cadastrarDisciplina(
            @RequestBody Map<String, Object> requisicao) {

        try {
            Disciplina disciplina = new Disciplina(
                    texto(requisicao.get("codigo")),
                    texto(requisicao.get("nome")),
                    cargaHoraria(requisicao.get("cargaHoraria"))
            );

            if (disciplinaRepository.buscarPorCodigo(disciplina.getCodigo()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("erro", "Disciplina ja cadastrada."));
            }

            disciplinaRepository.salvar(disciplina);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(respostaDisciplina(disciplina));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", e.getMessage()));
        }
    }

    private String texto(Object valor) {
        return valor == null ? null : valor.toString();
    }

    private int cargaHoraria(Object valor) {
        if (valor == null || valor.toString().isBlank()) {
            throw new IllegalArgumentException("A carga horaria e obrigatoria.");
        }

        try {
            return Integer.parseInt(valor.toString());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("A carga horaria deve ser um numero inteiro.");
        }
    }

    private Map<String, Object> respostaDisciplina(Disciplina disciplina) {
        return Map.of(
                "codigo", disciplina.getCodigo(),
                "nome", disciplina.getNome(),
                "cargaHoraria", disciplina.getCargaHoraria()
        );
    }
}
