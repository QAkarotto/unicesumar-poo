package br.edu.sistemaacademico;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    private final DisciplinaRepository disciplinaRepository;

    public DisciplinaController(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    @GetMapping("/{codigo}")
    public DisciplinaResponse buscarPorCodigo(@PathVariable String codigo) {
        Disciplina disciplina = disciplinaRepository.buscarPorCodigo(codigo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Disciplina não encontrada."
                ));

        return new DisciplinaResponse(
                disciplina.getCodigo(),
                disciplina.getNome(),
                disciplina.getCargaHoraria()
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DisciplinaResponse criar(@RequestBody NovaDisciplinaRequest request) {
        if (request == null
                || request.codigo() == null || request.codigo().isBlank()
                || request.nome() == null || request.nome().isBlank()
                || request.cargaHoraria() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Dados da disciplina inválidos ou incompletos."
            );
        }

        if (disciplinaRepository.buscarPorCodigo(request.codigo()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe uma disciplina cadastrada com este código."
            );
        }

        try {
            Disciplina disciplina = new Disciplina(
                    request.codigo(),
                    request.nome(),
                    request.cargaHoraria()
            );

            disciplinaRepository.salvar(disciplina);

            return new DisciplinaResponse(
                    disciplina.getCodigo(),
                    disciplina.getNome(),
                    disciplina.getCargaHoraria()
            );

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
        }
    }

    public record DisciplinaResponse(
            String codigo,
            String nome,
            int cargaHoraria
    ) {}

    public record NovaDisciplinaRequest(
            String codigo,
            String nome,
            int cargaHoraria
    ) {}
}