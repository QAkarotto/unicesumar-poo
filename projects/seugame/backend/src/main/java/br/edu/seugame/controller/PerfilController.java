package br.edu.seugame.controller;

import br.edu.seugame.dto.ContaDtos.*;
import br.edu.seugame.service.ContaService;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/usuarios/me") @Profile("!infra")
public class PerfilController {
    private final ContaService contas;
    public PerfilController(ContaService contas) { this.contas=contas; }
    @GetMapping public UsuarioResponse consultar() { return contas.perfil(); }
    @GetMapping("/torneios") public java.util.List<MeuTorneioResponse> torneios() { return contas.meusTorneios(); }
    @PatchMapping public UsuarioResponse alterar(@Valid @RequestBody PerfilRequest request) { return contas.alterarPerfil(request); }
}
