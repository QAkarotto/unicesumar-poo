package br.edu.aniva.controller;

import br.edu.aniva.dto.BibliotecaResponse;
import br.edu.aniva.dto.PautaRequest;
import br.edu.aniva.service.BibliotecaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Lista pessoal da pessoa logada.
@RestController
@RequestMapping("/api/biblioteca")
public class BibliotecaController {

    private final BibliotecaService service;
    private final SessaoUsuario sessao;

    public BibliotecaController(BibliotecaService service, SessaoUsuario sessao) {
        this.service = service;
        this.sessao = sessao;
    }

    @GetMapping
    public BibliotecaResponse buscar(HttpServletRequest http) {
        return service.buscar(sessao.exigirId(http));
    }

    @PostMapping("/favoritos/{obraId}/alternar")
    public BibliotecaResponse alternarFavorito(@PathVariable String obraId, HttpServletRequest http) {
        return service.alternarFavorito(sessao.exigirId(http), obraId);
    }

    @PostMapping("/pauta/{obraId}/alternar")
    public BibliotecaResponse alternarPauta(@PathVariable String obraId, HttpServletRequest http) {
        return service.alternarPauta(sessao.exigirId(http), obraId);
    }

    @PatchMapping("/pauta/{obraId}")
    public BibliotecaResponse atualizarPauta(@PathVariable String obraId,
                                             @Valid @RequestBody PautaRequest request,
                                             HttpServletRequest http) {
        return service.atualizarPauta(sessao.exigirId(http), obraId, request);
    }
}
