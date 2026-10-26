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
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    private final DisciplinaRepository repository;

    public DisciplinaController(DisciplinaRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<DisciplinaResponse> criar(@RequestBody CriarDisciplinaRequest request) {
        if (request == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Disciplina disciplina = new Disciplina(request.codigo(), request.nome(), request.cargaHoraria());
            repository.salvar(disciplina);
            var location = UriComponentsBuilder.fromPath("/api/disciplinas")
                    .pathSegment(disciplina.getCodigo()).build().encode().toUri();
            return ResponseEntity.created(location).body(DisciplinaResponse.from(disciplina));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<DisciplinaResponse> buscar(@PathVariable String codigo) {
        return repository.buscarPorCodigo(codigo)
                .map(DisciplinaResponse::from)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
