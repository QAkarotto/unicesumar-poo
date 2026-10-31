package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

/**
 * Recebe e responde as requisições HTTP de disciplinas.
 * Não contém SQL: o acesso aos dados é delegado ao {@link DisciplinaRepository}.
 */
@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {
    private final DisciplinaRepository disciplinas;

    public DisciplinaController(DisciplinaRepository disciplinas) {
        this.disciplinas = disciplinas;
    }

    @PostMapping
    public ResponseEntity<DisciplinaResponse> cadastrar(@RequestBody CadastrarDisciplinaRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Informe codigo, nome e cargaHoraria.");
        }
        if (request.cargaHoraria() == null) {
            throw new IllegalArgumentException("A carga horária é obrigatória.");
        }
        // O construtor do domínio valida os dados (IllegalArgumentException → 400).
        var disciplina = new Disciplina(request.codigo(), request.nome(), request.cargaHoraria());
        var salva = disciplinas.salvar(disciplina);
        var location = URI.create("/api/disciplinas/"
                + UriUtils.encodePathSegment(salva.getCodigo(), StandardCharsets.UTF_8));
        return ResponseEntity.created(location).body(DisciplinaResponse.de(salva));
    }

    @GetMapping("/{codigo}")
    public DisciplinaResponse buscar(@PathVariable("codigo") String codigo) {
        return disciplinas.buscarPorCodigo(codigo)
                .map(DisciplinaResponse::de)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Disciplina não encontrada."));
    }

    public record CadastrarDisciplinaRequest(String codigo, String nome, Integer cargaHoraria) {}

    public record DisciplinaResponse(String codigo, String nome, int cargaHoraria) {
        static DisciplinaResponse de(Disciplina disciplina) {
            return new DisciplinaResponse(disciplina.getCodigo(), disciplina.getNome(),
                    disciplina.getCargaHoraria());
        }
    }
}
