
package br.edu.biblioteca.dto;

import br.edu.biblioteca.domain.Livro;

public record LivroResponse(
    Long id,
    String titulo,
    String autor,
    String isbn,
    Integer quantidadeTotal,
    Integer quantidadeDisponivel
) {

    public static LivroResponse fromEntity(Livro livro) {
        return new LivroResponse(
            livro.getId(),
            livro.getTitulo(),
            livro.getAutor(),
            livro.getIsbn(),
            livro.getQuantidadeTotal(),
            livro.getQuantidadeDisponivel()
        );
    }
}
