package br.edu.seugame.controller;

import br.edu.seugame.domain.*;
import br.edu.seugame.dto.TorneioDtos.*;
import br.edu.seugame.service.TorneioService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/torneios") @Profile("!infra")
public class TorneioController {
    private final TorneioService service;
    public TorneioController(TorneioService service) {this.service=service;}
    @GetMapping public List<ResumoResponse> listar(@RequestParam(required=false) String busca,
            @RequestParam(required=false) TipoTorneio tipo,@RequestParam(required=false) StatusTorneio status) {return service.listar(busca,tipo,status);}
    @GetMapping("/{id}") public TorneioResponse consultar(@PathVariable Long id) {return service.consultar(id);}
    @PostMapping public ResponseEntity<TorneioResponse> criar(@Valid @RequestBody TorneioRequest request) {
        var resposta=service.criar(request);return ResponseEntity.created(URI.create("/api/torneios/"+resposta.torneio().id())).body(resposta);
    }
    @PutMapping("/{id}") public TorneioResponse editar(@PathVariable Long id,@Valid @RequestBody TorneioRequest request) {return service.editar(id,request);}
    @DeleteMapping("/{id}") public ResponseEntity<Void> excluir(@PathVariable Long id) {service.excluir(id);return ResponseEntity.noContent().build();}
    @GetMapping("/{id}/partidas") public List<PartidaResponse> partidas(@PathVariable Long id) {return service.partidas(id);}
    @GetMapping("/{id}/classificacao") public List<ClassificacaoResponse> classificacao(@PathVariable Long id) {return service.classificacao(id);}
    @GetMapping("/{id}/chaveamento") public List<RodadaResponse> chaveamento(@PathVariable Long id) {return service.chaveamento(id);}
    @PutMapping("/{id}/partidas/{partidaId}/resultado") public List<PartidaResponse> resultado(@PathVariable Long id,@PathVariable Long partidaId,
            @Valid @RequestBody ResultadoRequest request) {return service.registrarResultado(id,partidaId,request);}
    @PostMapping("/{id}/finalizacao") public TorneioResponse finalizar(@PathVariable Long id) {return service.finalizar(id);}
}
