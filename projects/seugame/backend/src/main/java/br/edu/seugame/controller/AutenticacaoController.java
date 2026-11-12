package br.edu.seugame.controller;

import br.edu.seugame.dto.ContaDtos.*;
import br.edu.seugame.service.ContaService;
import jakarta.servlet.http.*;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/auth") @Profile("!infra")
@ConditionalOnWebApplication(type=ConditionalOnWebApplication.Type.SERVLET)
public class AutenticacaoController {
    private final ContaService contas;
    private final AuthenticationManager manager;
    private final SessionAuthenticationStrategy sessions;
    private final SecurityContextRepository contexts;
    public AutenticacaoController(ContaService contas,AuthenticationManager manager,SessionAuthenticationStrategy sessions,SecurityContextRepository contexts) {
        this.contas=contas;this.manager=manager;this.sessions=sessions;this.contexts=contexts;
    }
    @GetMapping("/csrf") public CsrfResponse csrf(CsrfToken token) { return new CsrfResponse(token.getHeaderName(),token.getToken()); }
    @PostMapping("/cadastro") public ResponseEntity<UsuarioResponse> cadastrar(@Valid @RequestBody CadastroRequest dados,HttpServletRequest req,HttpServletResponse res) {
        var u=contas.cadastrar(dados); autenticar(dados.usuario(),dados.senha(),req,res);
        return ResponseEntity.created(URI.create("/api/usuarios/me")).body(u);
    }
    @PostMapping("/login") public UsuarioResponse login(@Valid @RequestBody LoginRequest dados,HttpServletRequest req,HttpServletResponse res) {
        autenticar(dados.identificador(),dados.senha(),req,res); return contas.perfil();
    }
    private void autenticar(String identificador,String senha,HttpServletRequest req,HttpServletResponse res) {
        var auth=manager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(identificador,senha));
        sessions.onAuthentication(auth,req,res);
        var context=SecurityContextHolder.createEmptyContext();context.setAuthentication(auth);
        SecurityContextHolder.setContext(context); contexts.saveContext(context,req,res);
    }
}
