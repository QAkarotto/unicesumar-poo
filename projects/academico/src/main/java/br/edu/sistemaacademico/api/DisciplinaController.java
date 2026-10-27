package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
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

    // POST /api/disciplinas -> 201, 400 JSON incompleto, 409 código duplicado, 422 regra do domínio
    @PostMapping
    public ResponseEntity<DisciplinaResponse> cadastrar(@RequestBody NovaDisciplinaRequest dados) {
        if (dados.cargaHoraria() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "O campo 'cargaHoraria' é obrigatório.");
        }
        Disciplina disciplina = new Disciplina(dados.codigo(), dados.nome(), dados.cargaHoraria());
        disciplinas.salvar(disciplina);
        URI local = URI.create("/api/disciplinas/" + disciplina.getCodigo());
        return ResponseEntity.created(local).body(DisciplinaResponse.de(disciplina));
    }

    // GET /api/disciplinas/POO -> 200 ou 404
    @GetMapping("/{codigo}")
    public DisciplinaResponse consultar(@PathVariable("codigo") String codigo) {
        return disciplinas.buscarPorCodigo(codigo)
                .map(DisciplinaResponse::de)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Disciplina não encontrada: " + codigo));
    }
}
