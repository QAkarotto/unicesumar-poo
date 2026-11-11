package br.edu.biblioteca.dto;

import br.edu.biblioteca.domain.Usuario;

public record UsuarioResponse(Long id, String nome, String email) {

    public static UsuarioResponse from(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getNome(), u.getEmail());
    }
}