package br.edu.biblioteca.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "emprestimo")
public class Emprestimo {

    public static final int LIMITE_EMPRESTIMOS_ATIVOS = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "livro_id")
    private Livro livro;

    @Column(name = "data_emprestimo", nullable = false)
    private LocalDate dataEmprestimo;

    @Column(name = "data_devolucao")
    private LocalDate dataDevolucao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SituacaoEmprestimo situacao;

    protected Emprestimo() {}

    public Emprestimo(Usuario usuario, Livro livro, LocalDate dataEmprestimo) {
        this.usuario = usuario;
        this.livro = livro;
        this.dataEmprestimo = dataEmprestimo;
        this.situacao = SituacaoEmprestimo.ATIVO;
    }

    /**
     * Regras para criar um novo empréstimo.
     * Recebe os dados já consultados e lança IllegalStateException se alguma regra for violada.
     */
    public static void validarNovo(long emprestimosAtivosDoUsuario, boolean livroJaEmprestado) {
        if (emprestimosAtivosDoUsuario >= LIMITE_EMPRESTIMOS_ATIVOS) {
            throw new IllegalStateException(
                    "O usuário já possui " + LIMITE_EMPRESTIMOS_ATIVOS + " empréstimos ativos.");
        }
        if (livroJaEmprestado) {
            throw new IllegalStateException("O livro já está emprestado.");
        }
    }

    /** Regra da devolução: só é possível devolver um empréstimo ativo. */
    public void devolver(LocalDate data) {
        if (situacao != SituacaoEmprestimo.ATIVO) {
            throw new IllegalStateException("Este empréstimo já foi devolvido.");
        }
        if (data.isBefore(dataEmprestimo)) {
            throw new IllegalArgumentException("A data de devolução não pode ser anterior ao empréstimo.");
        }
        this.dataDevolucao = data;
        this.situacao = SituacaoEmprestimo.DEVOLVIDO;
    }

    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public Livro getLivro() { return livro; }
    public LocalDate getDataEmprestimo() { return dataEmprestimo; }
    public LocalDate getDataDevolucao() { return dataDevolucao; }
    public SituacaoEmprestimo getSituacao() { return situacao; }
}