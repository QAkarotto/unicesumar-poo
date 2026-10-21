package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.persistencia.DisciplinaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {
    private final DisciplinaRepository disciplinas;

    public DisciplinaController(DisciplinaRepository disciplinas) {
        this.disciplinas = disciplinas;
    }

    @PostMapping
    public ResponseEntity<DisciplinaResponse> cadastrar(@RequestBody CriarDisciplinaRequest request) {
        // Integer pra diferenciar "nao enviado" de zero; sem isso a ausencia virava 0 sem aviso
        if (request.cargaHoraria() == null) {
            throw new IllegalArgumentException("A carga horária é obrigatória.");
        }
        var disciplina = disciplinas.salvar(new Disciplina(request.codigo(), request.nome(), request.cargaHoraria()));
        var location = UriComponentsBuilder.fromPath("/api/disciplinas/{codigo}")
                .buildAndExpand(disciplina.getCodigo()).encode().toUri();
        return ResponseEntity.created(location).body(DisciplinaResponse.de(disciplina));
    }

    @GetMapping("/{codigo}")
    public DisciplinaResponse buscar(@PathVariable("codigo") String codigo) {
        return disciplinas.buscarPorCodigo(codigo)
                .map(DisciplinaResponse::de)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Disciplina não encontrada."));
    }

    public record CriarDisciplinaRequest(String codigo, String nome, Integer cargaHoraria) {}

    public record DisciplinaResponse(String codigo, String nome, int cargaHoraria) {
        static DisciplinaResponse de(Disciplina disciplina) {
            return new DisciplinaResponse(disciplina.getCodigo(), disciplina.getNome(), disciplina.getCargaHoraria());
        }
    }
}
