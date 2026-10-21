package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    private final DisciplinaRepository disciplinas;

    public DisciplinaController(DisciplinaRepository disciplinas) {
        this.disciplinas = disciplinas;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody NovaDisciplinaRequisicao requisicao) {
        if (vazio(requisicao.codigo()) || vazio(requisicao.nome())
                || requisicao.cargaHoraria() == null || requisicao.cargaHoraria() <= 0) {
            return ResponseEntity.badRequest()
                    .body(new ErroResposta("Informe codigo, nome e cargaHoraria (maior que zero)."));
        }

        try {
            Disciplina disciplina = disciplinas.salvar(new Disciplina(
                    requisicao.codigo(), requisicao.nome(), requisicao.cargaHoraria()));
            return ResponseEntity
                    .created(URI.create("/api/disciplinas/" + disciplina.getCodigo()))
                    .body(DisciplinaResposta.de(disciplina));
        } catch (RegistroDuplicadoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErroResposta(e.getMessage()));
        }
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<?> consultar(@PathVariable String codigo) {
        return disciplinas.buscarPorCodigo(codigo)
                .<ResponseEntity<?>>map(d -> ResponseEntity.ok(DisciplinaResposta.de(d)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErroResposta("Disciplina não encontrada: " + codigo)));
    }

    private boolean vazio(String texto) {
        return texto == null || texto.isBlank();
    }
}
