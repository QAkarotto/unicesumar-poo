package br.edu.biblioteca.domain;

import br.edu.biblioteca.exception.RegraNegocioException;
import jakarta.persistence.*;

@Entity
@Table(name = "livro")
public class Livro {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 180)
    private String titulo;
    @Column(nullable = false, length = 120)
    private String autor;
    @Column(nullable = false, unique = true, length = 13)
    private String isbn;
    @Column(nullable = false, length = 60)
    private String categoria;
    @Column(nullable = false)
    private int exemplares;

    protected Livro() {}

    public Livro(String titulo, String autor, String isbn, String categoria, int exemplares) {
        atualizar(titulo, autor, isbn, categoria, exemplares, 0);
    }

    public void atualizar(String titulo, String autor, String isbn, String categoria, int exemplares, long emUso) {
        if (titulo == null || titulo.isBlank() || autor == null || autor.isBlank()
                || categoria == null || categoria.isBlank() || isbn == null || !isbn.matches("\\d{13}")) {
            throw new RegraNegocioException("Informe título, autor, categoria e ISBN com 13 dígitos.");
        }
        if (exemplares < 1 || exemplares < emUso) {
            throw new RegraNegocioException("O total deve ser positivo e não pode ser menor que os exemplares emprestados.");
        }
        this.titulo = titulo.strip(); this.autor = autor.strip(); this.isbn = isbn;
        this.categoria = categoria.strip(); this.exemplares = exemplares;
    }

    public void verificarDisponibilidade(long emUso) {
        if (emUso >= exemplares) throw new RegraNegocioException("Este livro não possui exemplares disponíveis.");
    }

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getIsbn() { return isbn; }
    public String getCategoria() { return categoria; }
    public int getExemplares() { return exemplares; }
}
