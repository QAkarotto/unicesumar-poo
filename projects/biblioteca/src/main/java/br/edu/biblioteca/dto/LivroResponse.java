package br.edu.biblioteca.dto;

import br.edu.biblioteca.domain.Livro;

public record LivroResponse(Long id, String titulo, String autor, String isbn) {

    public static LivroResponse from(Livro l) {
        return new LivroResponse(l.getId(), l.getTitulo(), l.getAutor(), l.getIsbn());
    }
}