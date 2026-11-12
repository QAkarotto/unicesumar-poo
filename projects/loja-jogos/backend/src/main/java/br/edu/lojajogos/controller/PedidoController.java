package br.edu.lojajogos.controller;

import br.edu.lojajogos.dto.CriarPedidoRequest;
import br.edu.lojajogos.dto.PedidoResponse;
import br.edu.lojajogos.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "http://localhost:5173")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @GetMapping
    public List<PedidoResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public PedidoResponse buscar(@PathVariable("id") Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> comprar(@RequestBody CriarPedidoRequest request) {
        var criado = service.comprar(request);
        return ResponseEntity.created(URI.create("/api/pedidos/" + criado.id())).body(criado);
    }

    // PATCH porque muda só uma parte do pedido (o status), não o pedido inteiro.
    @PatchMapping("/{id}/cancelamento")
    public PedidoResponse cancelar(@PathVariable("id") Long id) {
        return service.cancelar(id);
    }
}