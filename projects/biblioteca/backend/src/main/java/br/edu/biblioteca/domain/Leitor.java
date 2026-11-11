package br.edu.biblioteca.domain;

import br.edu.biblioteca.exception.RegraNegocioException;
import jakarta.persistence.*;
import java.util.Locale;

@Entity
@Table(name = "leitor")
public class Leitor {
    public static final int LIMITE_EMPRESTIMOS = 3;
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 120)
    private String nome;
    @Column(nullable = false, unique = true, length = 180)
    private String email;
    @Column(nullable = false)
    private boolean ativo = true;

    protected Leitor() {}
    public Leitor(String nome, String email) { atualizar(nome, email); }

    public void atualizar(String nome, String email) {
        if (nome == null || nome.isBlank() || email == null || email.isBlank()) {
            throw new RegraNegocioException("Nome e e-mail são obrigatórios.");
        }
        this.nome = nome.strip(); this.email = email.strip().toLowerCase(Locale.ROOT);
    }

    public void verificarElegibilidade(long ativos, boolean possuiAtraso) {
        if (!ativo) throw new RegraNegocioException("Leitor inativo não pode realizar empréstimos.");
        if (possuiAtraso) throw new RegraNegocioException("Devolva os livros em atraso antes de realizar um novo empréstimo.");
        if (ativos >= LIMITE_EMPRESTIMOS) throw new RegraNegocioException("O leitor já atingiu o limite de 3 empréstimos ativos.");
    }

    public void alterarAtividade(boolean ativo, long emprestimosAtivos) {
        if (!ativo && emprestimosAtivos > 0) throw new RegraNegocioException("Leitores com empréstimos ativos não podem ser inativados.");
        this.ativo = ativo;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public boolean isAtivo() { return ativo; }
}
