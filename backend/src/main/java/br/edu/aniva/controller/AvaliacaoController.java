package br.edu.aniva.controller;

import br.edu.aniva.dto.AvaliacaoRequest;
import br.edu.aniva.service.AvaliacaoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/obras/{obraId}/avaliacoes")
public class AvaliacaoController {

    private final AvaliacaoService service;
    private final SessaoUsuario sessao;

    public AvaliacaoController(AvaliacaoService service, SessaoUsuario sessao) {
        this.service = service;
        this.sessao = sessao;
    }

    @GetMapping
    public Map<Long, Integer> listar(@PathVariable String obraId) {
        return service.listar(obraId);
    }

    @PutMapping
    public Map<Long, Integer> avaliar(@PathVariable String obraId,
                                      @Valid @RequestBody AvaliacaoRequest request,
                                      HttpServletRequest http) {
        return service.avaliar(sessao.exigirId(http), obraId, request.nota());
    }

    @DeleteMapping
    public Map<Long, Integer> remover(@PathVariable String obraId, HttpServletRequest http) {
        return service.remover(sessao.exigirId(http), obraId);
    }
}
