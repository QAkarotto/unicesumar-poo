package br.edu.agroassist.controller;

import br.edu.agroassist.dto.Respostas.BancoResponse;
import br.edu.agroassist.service.ConsultaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dados")
public class ConsultaController {
    private final ConsultaService service;

    public ConsultaController(ConsultaService service) {
        this.service = service;
    }

    @GetMapping
    public BancoResponse carregar() {
        return service.carregar();
    }
}
