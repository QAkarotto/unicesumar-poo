package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.DisciplinaRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    private final DisciplinaRepository disciplinas;

    public DisciplinaController(DisciplinaRepository disciplinas) {
        this.disciplinas = disciplinas;
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<DisciplinaResponse> cadastrar(
            @RequestBody NovaDisciplinaRequest request
    ) {
        // O construtor do domínio valida os dados (IllegalArgumentException -> 400).
        Disciplina disciplina = new Disciplina(
                request.codigo(),
                request.nome(),
                request.cargaHoraria()
        );

        disciplinas.salvar(disciplina); // código repetido -> RegistroDuplicadoException -> 409

        URI local = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{codigo}")
                .buildAndExpand(disciplina.getCodigo())
                .toUri();

        return ResponseEntity
                .created(local)
                .body(DisciplinaResponse.de(disciplina));
    }

    @GetMapping(
            value = "/{codigo}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> consultar(@PathVariable("codigo") String codigo) {
        return disciplinas.buscarPorCodigo(codigo)
                .<ResponseEntity<?>>map(disciplina -> ResponseEntity.ok(DisciplinaResponse.de(disciplina)))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(Map.of("erro", "Disciplina não encontrada.")));
    }
}
