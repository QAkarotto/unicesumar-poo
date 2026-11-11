package br.edu.agroassist.controller;

import br.edu.agroassist.service.DemoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/demo")
public class DemoController {
    private final DemoService service;

    public DemoController(DemoService service) {
        this.service = service;
    }

    @PostMapping("/reiniciar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reiniciar(@RequestHeader("X-Usuario-Id") String autor) {
        service.reiniciar(autor);
    }
}
