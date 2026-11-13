package br.edu.biblioteca.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "livro")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String isbn;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, length = 160)
    private String autor;

    @Column(name = "quantidade_total", nullable = false)
    private int quantidadeTotal;

    @Column(name = "quantidade_disponivel", nullable = false)
    private int quantidadeDisponivel;

    protected Livro() {
    }

    public Livro(String isbn, String titulo, String autor, int quantidadeTotal) {
        if (quantidadeTotal < 1) {
            throw new IllegalArgumentException("Quantidade total deve ser pelo menos 1.");
        }
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.quantidadeTotal = quantidadeTotal;
        this.quantidadeDisponivel = quantidadeTotal;
    }

    public void atualizarDados(String titulo, String autor) {
        this.titulo = titulo;
        this.autor = autor;
    }

    public void emprestarExemplar() {
        if (quantidadeDisponivel == 0) {
            throw new IllegalStateException("Não há exemplares disponíveis para empréstimo.");
        }
        quantidadeDisponivel--;
    }

    public void devolverExemplar() {
        if (quantidadeDisponivel == quantidadeTotal) {
            throw new IllegalStateException("Todos os exemplares já estão disponíveis.");
        }
        quantidadeDisponivel++;
    }

    public Long getId() {
        return id;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public int getQuantidadeTotal() {
        return quantidadeTotal;
    }

    public int getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }
}
