package br.edu.seugame.security;

import br.edu.seugame.domain.Torneio;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SessaoUsuario {
    private SessaoUsuario() {}
    public static Long id() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof UsuarioAutenticado u))
            throw new AuthenticationCredentialsNotFoundException("Autenticação necessária.");
        return u.getId();
    }
    public static void exigirAutoria(Torneio torneio) {
        if (!id().equals(torneio.getCriador().getId())) throw new AccessDeniedException("Somente o criador pode gerenciar o torneio.");
    }
}
