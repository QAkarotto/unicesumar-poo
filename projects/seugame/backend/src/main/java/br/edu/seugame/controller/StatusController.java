package br.edu.seugame.controller;

import br.edu.seugame.dto.StatusResponse;
import br.edu.seugame.service.StatusService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/status")
public class StatusController {
    private final StatusService service;

    public StatusController(StatusService service) {
        this.service = service;
    }

    @GetMapping
    public StatusResponse consultar() {
        return service.consultar();
    }
}
