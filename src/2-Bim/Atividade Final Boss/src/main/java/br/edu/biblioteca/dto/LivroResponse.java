package br.edu.biblioteca.dto;

import br.edu.biblioteca.domain.Livro;

public record LivroResponse(
        Long id,
        String isbn,
        String titulo,
        String autor,
        int quantidadeTotal,
        int quantidadeDisponivel
) {
    public static LivroResponse from(Livro livro) {
        return new LivroResponse(
                livro.getId(),
                livro.getIsbn(),
                livro.getTitulo(),
                livro.getAutor(),
                livro.getQuantidadeTotal(),
                livro.getQuantidadeDisponivel()
        );
    }
}
