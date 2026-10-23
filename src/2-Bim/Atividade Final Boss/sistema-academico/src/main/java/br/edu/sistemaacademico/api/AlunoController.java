package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.memoria.PontedaApi;
import br.edu.sistemaacademico.memoria.RecursoNaoEncontradoException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final PontedaApi pontedaApi;

    public AlunoController(PontedaApi pontedaApi) {
        this.pontedaApi = pontedaApi;
    }

    @GetMapping("/{identificador}")
    public AlunoResponse consultar(@PathVariable String identificador) {
        return pontedaApi.buscarAluno(identificador)
                .map(AlunoResponse::de)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado"));
    }
}
