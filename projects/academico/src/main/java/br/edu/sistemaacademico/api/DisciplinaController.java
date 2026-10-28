package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    private final DisciplinaRepository disciplinaRepository;

    public DisciplinaController(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    @GetMapping
    public List<Disciplina> listarTodas() {
        return disciplinaRepository.listarTodas();
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(
            @RequestBody NovaDisciplina requisicao
    ) {
        try {
            Disciplina disciplina = new Disciplina(
                    requisicao.codigo(),
                    requisicao.nome(),
                    requisicao.cargaHoraria()
            );

            disciplinaRepository.salvar(disciplina);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(disciplina);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", e.getMessage()));

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "erro",
                            "Nao foi possivel cadastrar a disciplina. Verifique se o codigo ja existe."
                    ));
        }
    }

    public record NovaDisciplina(
            String codigo,
            String nome,
            int cargaHoraria
    ) {
    }
}
