package br.edu.agroassist.controller;

import br.edu.agroassist.dto.Requisicoes.*;
import br.edu.agroassist.dto.Respostas.*;
import br.edu.agroassist.service.SolicitacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/solicitacoes")
public class SolicitacaoController {
    private final SolicitacaoService service;

    public SolicitacaoController(SolicitacaoService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public SolicitacaoResponse buscar(@PathVariable String id) {
        return service.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SolicitacaoResponse abrir(
            @RequestHeader("X-Usuario-Id") String autor, @Valid @RequestBody DadosSolicitacao dados) {
        return service.abrir(autor, dados);
    }

    @PatchMapping("/{id}/atribuicao")
    public SolicitacaoResponse atribuir(
            @RequestHeader("X-Usuario-Id") String autor,
            @PathVariable String id,
            @Valid @RequestBody Atribuicao dados) {
        return service.atribuir(autor, id, dados.tecnicoId());
    }

    @PatchMapping("/{id}/inicio")
    public SolicitacaoResponse iniciar(@RequestHeader("X-Usuario-Id") String autor, @PathVariable String id) {
        return service.iniciar(autor, id);
    }

    @PostMapping("/{id}/atendimento")
    @ResponseStatus(HttpStatus.CREATED)
    public AtendimentoResponse concluir(
            @RequestHeader("X-Usuario-Id") String autor,
            @PathVariable String id,
            @Valid @RequestBody DadosAtendimento dados) {
        return service.concluir(autor, id, dados);
    }

    @PostMapping("/{id}/avaliacao")
    @ResponseStatus(HttpStatus.CREATED)
    public AvaliacaoResponse avaliar(
            @RequestHeader("X-Usuario-Id") String autor,
            @PathVariable String id,
            @Valid @RequestBody DadosAvaliacao dados) {
        return service.avaliar(autor, id, dados);
    }
}
