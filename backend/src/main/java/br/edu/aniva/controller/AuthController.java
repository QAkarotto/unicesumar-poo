package br.edu.aniva.controller;

import br.edu.aniva.dto.CadastroRequest;
import br.edu.aniva.dto.LoginRequest;
import br.edu.aniva.dto.UsuarioResponse;
import br.edu.aniva.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final SessaoUsuario sessao;

    public AuthController(UsuarioService usuarioService, SessaoUsuario sessao) {
        this.usuarioService = usuarioService;
        this.sessao = sessao;
    }

    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse cadastrar(@Valid @RequestBody CadastroRequest request, HttpServletRequest http) {
        UsuarioResponse usuario = usuarioService.cadastrar(request);
        sessao.iniciar(http, usuario.id());
        return usuario;
    }

    @PostMapping("/login")
    public UsuarioResponse login(@Valid @RequestBody LoginRequest request, HttpServletRequest http) {
        UsuarioResponse usuario = usuarioService.login(request);
        sessao.iniciar(http, usuario.id());
        return usuario;
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletRequest http) {
        sessao.encerrar(http);
    }

    // 200 com a pessoa logada, ou 204 se ninguém estiver logado.
    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> me(HttpServletRequest http) {
        return sessao.idOpcional(http)
                .flatMap(usuarioService::buscarPorId)
                .map(usuario -> ResponseEntity.ok(usuario))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
