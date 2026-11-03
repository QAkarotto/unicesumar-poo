package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.Disciplina;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    private final DisciplinaRepository disciplinaRepository;

    public DisciplinaController(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody NovaDisciplinaRequest req) {
        if (req.cargaHoraria() == null) {
            return ResponseEntity.badRequest()
                    .body(new ErroResponse("A carga horária é obrigatória."));
        }

        // Regras (código/nome obrigatórios, carga horária positiva) ficam no domínio
        var disciplina = new Disciplina(req.codigo(), req.nome(), req.cargaHoraria());

        disciplinaRepository.salvar(disciplina);

        return ResponseEntity
                .created(URI.create("/api/disciplinas/" + disciplina.getCodigo()))
                .body(DisciplinaResponse.de(disciplina));
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<?> consultar(@PathVariable String codigo) {
        var disciplina = disciplinaRepository.buscarPorCodigo(codigo);

        if (disciplina.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErroResponse("Disciplina não encontrada: " + codigo));
        }

        return ResponseEntity.ok(DisciplinaResponse.de(disciplina.get()));
    }

    // Integer (e não int) para conseguir detectar quando o campo não foi enviado
    public record NovaDisciplinaRequest(String codigo, String nome, Integer cargaHoraria) {}

    public record DisciplinaResponse(String codigo, String nome, int cargaHoraria) {

        static DisciplinaResponse de(Disciplina d) {
            return new DisciplinaResponse(d.getCodigo(), d.getNome(), d.getCargaHoraria());
        }
    }
}
