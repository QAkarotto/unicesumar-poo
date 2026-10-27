package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.api.repository.DisciplinaRepository;
import br.edu.sistemaacademico.domain.Disciplina;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    private final DisciplinaRepository disciplinaRepository;

    public DisciplinaController(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    @PostMapping
    public ResponseEntity<DisciplinaResponse> cadastrar(@RequestBody NovaDisciplinaRequest requisicao) {
        Disciplina disciplina = new Disciplina(
                requisicao.codigo(),
                requisicao.nome(),
                requisicao.cargaHoraria());

        disciplinaRepository.salvar(disciplina);

        return ResponseEntity.status(HttpStatus.CREATED).body(converter(disciplina));
    }

    @GetMapping("/{codigo}")
    public DisciplinaResponse buscar(@PathVariable String codigo) {
        Disciplina disciplina = disciplinaRepository.buscarPorCodigo(codigo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Disciplina não encontrada."));

        return converter(disciplina);
    }

    private DisciplinaResponse converter(Disciplina disciplina) {
        return new DisciplinaResponse(
                disciplina.getCodigo(),
                disciplina.getNome(),
                disciplina.getCargaHoraria());
    }
}