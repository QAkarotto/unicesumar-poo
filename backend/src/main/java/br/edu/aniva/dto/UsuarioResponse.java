package br.edu.aniva.dto;

import br.edu.aniva.domain.Usuario;

public record UsuarioResponse(Long id, String nome, String email) {

    public static UsuarioResponse de(Usuario usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }
}
