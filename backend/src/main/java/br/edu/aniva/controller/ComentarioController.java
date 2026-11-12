package br.edu.aniva.controller;

import br.edu.aniva.dto.ComentarioRequest;
import br.edu.aniva.dto.ComentarioResponse;
import br.edu.aniva.service.ComentarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/obras/{obraId}/comentarios")
public class ComentarioController {

    private final ComentarioService service;
    private final SessaoUsuario sessao;

    public ComentarioController(ComentarioService service, SessaoUsuario sessao) {
        this.service = service;
        this.sessao = sessao;
    }

    @GetMapping
    public List<ComentarioResponse> listar(@PathVariable String obraId) {
        return service.listar(obraId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<ComentarioResponse> comentar(@PathVariable String obraId,
                                             @Valid @RequestBody ComentarioRequest request,
                                             HttpServletRequest http) {
        return service.comentar(sessao.exigirId(http), obraId, request);
    }

    @PostMapping("/{comentarioId}/curtidas")
    public List<ComentarioResponse> alternarCurtida(@PathVariable String obraId,
                                                    @PathVariable Long comentarioId,
                                                    HttpServletRequest http) {
        return service.alternarCurtida(sessao.exigirId(http), obraId, comentarioId);
    }

    @DeleteMapping("/{comentarioId}")
    public List<ComentarioResponse> apagar(@PathVariable String obraId,
                                           @PathVariable Long comentarioId,
                                           HttpServletRequest http) {
        return service.apagar(sessao.exigirId(http), obraId, comentarioId);
    }
}
