package br.edu.biblioteca.domain;

import br.edu.biblioteca.exception.RegraNegocioException;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "emprestimo")
public class Emprestimo {
    public static final int PRAZO_DIAS = 14;
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "leitor_id", nullable = false)
    private Leitor leitor;
    @Column(name = "emprestado_em", nullable = false)
    private LocalDate emprestadoEm;
    @Column(name = "previsto_para", nullable = false)
    private LocalDate previstoPara;
    @Column(name = "devolvido_em")
    private LocalDate devolvidoEm;

    protected Emprestimo() {}
    public Emprestimo(Livro livro, Leitor leitor, LocalDate hoje) {
        this.livro = Objects.requireNonNull(livro); this.leitor = Objects.requireNonNull(leitor);
        this.emprestadoEm = Objects.requireNonNull(hoje); this.previstoPara = hoje.plusDays(PRAZO_DIAS);
    }

    public void devolver(LocalDate hoje) {
        if (devolvidoEm != null) throw new RegraNegocioException("Este empréstimo já foi devolvido.");
        if (hoje.isBefore(emprestadoEm)) throw new RegraNegocioException("A devolução não pode anteceder o empréstimo.");
        devolvidoEm = hoje;
    }

    public String situacao(LocalDate hoje) {
        if (devolvidoEm != null) return "DEVOLVIDO";
        return previstoPara.isBefore(hoje) ? "ATRASADO" : "ATIVO";
    }

    public Long getId() { return id; }
    public Livro getLivro() { return livro; }
    public Leitor getLeitor() { return leitor; }
    public LocalDate getEmprestadoEm() { return emprestadoEm; }
    public LocalDate getPrevistoPara() { return previstoPara; }
    public LocalDate getDevolvidoEm() { return devolvidoEm; }
}
