package br.edu.biblioteca.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "livro")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, length = 120)
    private String autor;

    @Column(nullable = false, unique = true, length = 20)
    private String isbn;

    protected Livro() {}

    public Livro(String titulo, String autor, String isbn) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("O título do livro é obrigatório.");
        }
        if (autor == null || autor.isBlank()) {
            throw new IllegalArgumentException("O autor do livro é obrigatório.");
        }
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("O ISBN do livro é obrigatório.");
        }
        this.titulo = titulo.trim();
        this.autor = autor.trim();
        this.isbn = isbn.trim();
    }

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getIsbn() { return isbn; }
}