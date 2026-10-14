package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.DadosEmMemoria;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final DadosEmMemoria dadosEmMemoria;

    public AlunoController(DadosEmMemoria dadosEmMemoria) {
        this.dadosEmMemoria = dadosEmMemoria;
    }

    @GetMapping("/{identificador}")
    public ResponseEntity<Aluno> buscarPorIdentificador(@PathVariable String identificador) {
        Aluno aluno = dadosEmMemoria.buscarAlunoPorRa(identificador);

        if (aluno == null) {
            return ResponseEntity.notFound().build(); // HTTP 404
        }

        return ResponseEntity.ok(aluno); // HTTP 200
    }
}