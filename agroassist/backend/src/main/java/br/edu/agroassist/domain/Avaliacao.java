package br.edu.agroassist.domain;

import br.edu.agroassist.exception.DadosInvalidosException;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "avaliacoes")
public class Avaliacao {
    @Id
    @Column(length = 40)
    private String id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "solicitacao_id", nullable = false, unique = true)
    private Solicitacao solicitacao;

    @Column(nullable = false)
    private int nota;

    @Column(nullable = false, length = 2000)
    private String comentario;

    @Column(nullable = false)
    private LocalDateTime em;

    protected Avaliacao() {}

    Avaliacao(String id, Solicitacao solicitacao, int nota, String comentario, LocalDateTime em) {
        if (nota < 1 || nota > 5) throw new DadosInvalidosException("Escolha uma nota de 1 a 5.");
        this.id = id;
        this.solicitacao = solicitacao;
        this.nota = nota;
        this.comentario = Validacao.texto(comentario, 2000, "Comentário");
        this.em = Objects.requireNonNull(em);
    }

    public String getId() {
        return id;
    }

    public Solicitacao getSolicitacao() {
        return solicitacao;
    }

    public int getNota() {
        return nota;
    }

    public String getComentario() {
        return comentario;
    }

    public LocalDateTime getEm() {
        return em;
    }
}
