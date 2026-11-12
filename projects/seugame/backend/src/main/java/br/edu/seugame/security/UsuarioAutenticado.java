package br.edu.seugame.security;

import br.edu.seugame.domain.Usuario;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UsuarioAutenticado extends User {
    private static final long serialVersionUID = 1L;
    private final Long id;
    public UsuarioAutenticado(Usuario usuario) {
        super(usuario.getUsuario(), usuario.getSenhaHash(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        id = usuario.getId();
    }
    public Long getId() { return id; }
}
