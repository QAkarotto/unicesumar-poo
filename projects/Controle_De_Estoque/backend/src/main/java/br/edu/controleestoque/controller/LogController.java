package br.edu.controleestoque.controller;

import br.edu.controleestoque.dto.LogResponse;
import br.edu.controleestoque.service.LogService;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Somente consulta: propositalmente não há rotas para alterar ou excluir logs.
 */
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/logs")
public class LogController {

    private final LogService service;

    public LogController(LogService service) {
        this.service = service;
    }

    @GetMapping
    public List<LogResponse> listar() {
        return service.listar();
    }
}
