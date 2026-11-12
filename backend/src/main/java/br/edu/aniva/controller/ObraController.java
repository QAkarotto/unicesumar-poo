package br.edu.aniva.controller;

import br.edu.aniva.dto.ObraResponse;
import br.edu.aniva.service.ObraService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/obras")
public class ObraController {

    private final ObraService service;

    public ObraController(ObraService service) {
        this.service = service;
    }

    @GetMapping
    public List<ObraResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ObraResponse buscar(@PathVariable String id) {
        return service.buscar(id);
    }
}
