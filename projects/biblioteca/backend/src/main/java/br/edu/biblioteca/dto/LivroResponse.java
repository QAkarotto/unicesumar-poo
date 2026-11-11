package br.edu.biblioteca.dto;
import br.edu.biblioteca.domain.Livro;
public record LivroResponse(Long id, String titulo, String autor, String isbn, String categoria, int exemplares, long disponiveis) {
    public static LivroResponse de(Livro livro, long emUso) {
        return new LivroResponse(livro.getId(), livro.getTitulo(), livro.getAutor(), livro.getIsbn(), livro.getCategoria(), livro.getExemplares(), livro.getExemplares() - emUso);
    }
}
