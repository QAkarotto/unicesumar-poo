
package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.repository.MemoriaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final MemoriaRepository repository;

    public AlunoController(MemoriaRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{identificador}")
    public ResponseEntity<Aluno> buscarPorIdentificador(
            @PathVariable String identificador) {

        return repository.buscarAlunoPorIdentificador(identificador)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
