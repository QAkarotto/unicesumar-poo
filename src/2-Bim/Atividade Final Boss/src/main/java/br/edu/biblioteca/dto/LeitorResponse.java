package br.edu.biblioteca.dto;

import br.edu.biblioteca.domain.Leitor;

public record LeitorResponse(Long id, String nome, String email) {

    public static LeitorResponse from(Leitor leitor) {
        return new LeitorResponse(leitor.getId(), leitor.getNome(), leitor.getEmail());
    }
}
