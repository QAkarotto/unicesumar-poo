package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.dto.OfertaResponse;
import br.edu.sistemaacademico.service.OfertaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ofertas")
public class OfertaController {

    private final OfertaService service;

    public OfertaController(OfertaService service) {
        this.service = service;
    }

    @GetMapping
    public List<OfertaResponse> listar() {
        return service.listar();
    }
}
