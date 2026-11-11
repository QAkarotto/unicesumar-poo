package br.edu.agroassist.controller;

import br.edu.agroassist.dto.Requisicoes.*;
import br.edu.agroassist.dto.Respostas.*;
import br.edu.agroassist.service.CadastroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CadastroController {
    private final CadastroService service;

    public CadastroController(CadastroService service) {
        this.service = service;
    }

    @PostMapping("/produtores")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse criarProdutor(@Valid @RequestBody DadosConta dados) {
        return service.criarProdutor(dados);
    }

    @PostMapping("/propriedades")
    @ResponseStatus(HttpStatus.CREATED)
    public PropriedadeResponse criarPropriedade(
            @RequestHeader("X-Usuario-Id") String autor, @Valid @RequestBody DadosPropriedade dados) {
        return service.criarPropriedade(autor, dados);
    }

    @PostMapping("/tecnicos")
    @ResponseStatus(HttpStatus.CREATED)
    public TecnicoResponse criarTecnico(
            @RequestHeader("X-Usuario-Id") String autor, @Valid @RequestBody DadosTecnico dados) {
        return service.criarTecnico(autor, dados);
    }

    @PatchMapping("/tecnicos/{id}/situacao")
    public TecnicoResponse alterarSituacao(
            @RequestHeader("X-Usuario-Id") String autor,
            @PathVariable String id,
            @Valid @RequestBody Situacao dados) {
        return service.alterarSituacao(autor, id, dados.ativo());
    }
}
