package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    private final DisciplinaRepository disciplinas;

    public DisciplinaController(DisciplinaRepository disciplinas) {
        this.disciplinas = disciplinas;
    }

    // POST /api/disciplinas -> 201 Created, 400 se os dados forem inválidos, 409 se o código já existir.
    @PostMapping
    public ResponseEntity<DisciplinaResponse> cadastrar(@RequestBody DisciplinaRequest requisicao) {
        if (requisicao.cargaHoraria() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A carga horária é obrigatória.");
        }

        Disciplina disciplina;
        try {
            // O domínio valida os dados (código, nome e carga horária).
            disciplina = new Disciplina(
                    requisicao.codigo(), requisicao.nome(), requisicao.cargaHoraria());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        try {
            disciplinas.salvar(disciplina);
        } catch (RegistroDuplicadoException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }

        return ResponseEntity
                .created(URI.create("/api/disciplinas/" + disciplina.getCodigo()))
                .body(DisciplinaResponse.de(disciplina));
    }

    // GET /api/disciplinas/POO -> 200 com a disciplina, ou 404 se não existir.
    @GetMapping("/{codigo}")
    public DisciplinaResponse consultar(@PathVariable String codigo) {
        return disciplinas.buscarPorCodigo(codigo)
                .map(DisciplinaResponse::de)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Disciplina não encontrada: " + codigo));
    }
}
