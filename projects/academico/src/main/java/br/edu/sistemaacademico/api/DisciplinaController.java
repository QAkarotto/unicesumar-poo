package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {
    private final DisciplinaRepository disciplinaRepository;

    public DisciplinaController(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<DisciplinaResposta> consultar(@PathVariable String codigo) {
        return disciplinaRepository.buscarPorCodigo(codigo)
                .map(disciplina -> ResponseEntity.ok(DisciplinaResposta.de(disciplina)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody NovaDisciplinaRequisicao requisicao) {
        try {
            var disciplina = disciplinaRepository.salvar(
                    new Disciplina(requisicao.codigo(), requisicao.nome(), requisicao.cargaHoraria()));
            var localizacao = ServletUriComponentsBuilder.fromCurrentRequest().path("/{codigo}")
                    .buildAndExpand(disciplina.getCodigo()).toUri();
            return ResponseEntity.created(localizacao).body(DisciplinaResposta.de(disciplina));
        } catch (RegistroDuplicadoException excecao) {
            return erro(HttpStatus.CONFLICT, excecao.getMessage());
        } catch (IllegalArgumentException excecao) {
            return erro(HttpStatus.UNPROCESSABLE_ENTITY, excecao.getMessage());
        }
    }

    private ResponseEntity<Map<String, String>> erro(HttpStatus status, String mensagem) {
        return ResponseEntity.status(status).body(Map.of("erro", mensagem));
    }

    public record NovaDisciplinaRequisicao(String codigo, String nome, int cargaHoraria) { }

    public record DisciplinaResposta(String codigo, String nome, int cargaHoraria) {
        static DisciplinaResposta de(Disciplina disciplina) {
            return new DisciplinaResposta(disciplina.getCodigo(), disciplina.getNome(), disciplina.getCargaHoraria());
        }
    }
}
