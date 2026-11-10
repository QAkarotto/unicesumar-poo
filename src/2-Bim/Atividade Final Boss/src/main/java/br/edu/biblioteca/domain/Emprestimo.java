package br.edu.biblioteca.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "emprestimo")
public class Emprestimo {

    public static final int PRAZO_DIAS = 14;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "leitor_id")
    private Leitor leitor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "livro_id")
    private Livro livro;

    @Column(name = "data_emprestimo", nullable = false)
    private LocalDate dataEmprestimo;

    @Column(name = "data_prevista_devolucao", nullable = false)
    private LocalDate dataPrevistaDevolucao;

    @Column(name = "data_devolucao")
    private LocalDate dataDevolucao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SituacaoEmprestimo situacao;

    protected Emprestimo() {
    }

    public Emprestimo(Leitor leitor, Livro livro, LocalDate dataEmprestimo) {
        livro.emprestarExemplar();
        this.leitor = leitor;
        this.livro = livro;
        this.dataEmprestimo = dataEmprestimo;
        this.dataPrevistaDevolucao = dataEmprestimo.plusDays(PRAZO_DIAS);
        this.situacao = SituacaoEmprestimo.ATIVO;
    }

    public void devolver(LocalDate dataDevolucao) {
        if (situacao == SituacaoEmprestimo.DEVOLVIDO) {
            throw new IllegalStateException("Somente empréstimos ativos podem ser devolvidos.");
        }
        livro.devolverExemplar();
        this.dataDevolucao = dataDevolucao;
        this.situacao = SituacaoEmprestimo.DEVOLVIDO;
    }

    public Long getId() {
        return id;
    }

    public Leitor getLeitor() {
        return leitor;
    }

    public Livro getLivro() {
        return livro;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public SituacaoEmprestimo getSituacao() {
        return situacao;
    }
}
