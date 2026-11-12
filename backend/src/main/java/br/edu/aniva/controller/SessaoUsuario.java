package br.edu.aniva.controller;

import br.edu.aniva.exception.NaoAutenticadoException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.util.Optional;

//A identidade sempre vem daqui, nunca do corpo da requisição enviado pelo frontend.

@Component
public class SessaoUsuario {

    private static final String ATRIBUTO = "usuarioId";

    public void iniciar(HttpServletRequest request, Long usuarioId) {
        if (request.getSession(false) != null) {
            request.changeSessionId(); // evita fixação de sessão
        }
        request.getSession(true).setAttribute(ATRIBUTO, usuarioId);
    }

    public void encerrar(HttpServletRequest request) {
        HttpSession sessao = request.getSession(false);
        if (sessao != null) {
            sessao.invalidate();
        }
    }

    public Optional<Long> idOpcional(HttpServletRequest request) {
        HttpSession sessao = request.getSession(false);
        if (sessao != null && sessao.getAttribute(ATRIBUTO) instanceof Long id) {
            return Optional.of(id);
        }
        return Optional.empty();
    }

    // Devolve o id da pessoa logada ou responde 401.
    public Long exigirId(HttpServletRequest request) {
        return idOpcional(request)
                .orElseThrow(() -> new NaoAutenticadoException("Entre na sua conta para continuar."));
    }
}
