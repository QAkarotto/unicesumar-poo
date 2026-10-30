package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.api.Dtos.CriarDisciplinaRequest;
import br.edu.sistemaacademico.api.Dtos.DisciplinaResponse;
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
import org.springframework.web.util.UriComponentsBuilder;

/** Só traduz HTTP <-> domínio/Repository. Nenhum SQL aqui. */
@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    private final DisciplinaRepository disciplinas;

    public DisciplinaController(DisciplinaRepository disciplinas) {
        this.disciplinas = disciplinas;
    }

    // POST /api/disciplinas -> 201 | 400 campos ausentes | 409 código já cadastrado | 422 regra do domínio
    @PostMapping
    public ResponseEntity<DisciplinaResponse> cadastrar(@RequestBody CriarDisciplinaRequest requisicao) {
        if (requisicao == null || requisicao.codigo() == null || requisicao.codigo().isBlank()
                || requisicao.nome() == null || requisicao.nome().isBlank()
                || requisicao.cargaHoraria() == null) {
            throw new ErroApi(HttpStatus.BAD_REQUEST, "Informe codigo, nome e cargaHoraria.");
        }

        var disciplina = new Disciplina(requisicao.codigo(), requisicao.nome(), requisicao.cargaHoraria());
        var salva = disciplinas.salvar(disciplina);

        var local = UriComponentsBuilder.fromPath("/api/disciplinas/{codigo}")
                .encode().buildAndExpand(salva.getCodigo()).toUri();
        return ResponseEntity.created(local).body(DisciplinaResponse.de(salva));
    }

    // GET /api/disciplinas/POO -> 200 | 404
    @GetMapping("/{codigo}")
    public DisciplinaResponse consultar(@PathVariable String codigo) {
        return disciplinas.buscarPorCodigo(codigo)
                .map(DisciplinaResponse::de)
                .orElseThrow(() -> new ErroApi(HttpStatus.NOT_FOUND, "Disciplina não encontrada: " + codigo));
    }
}
