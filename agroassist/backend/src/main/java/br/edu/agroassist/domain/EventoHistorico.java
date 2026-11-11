package br.edu.agroassist.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;

@Embeddable
public class EventoHistorico {
    @Column(nullable = false)
    private LocalDateTime em;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, length = 30)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "autor_id", nullable = false)
    private Usuario autor;

    @Column(nullable = false, length = 255)
    private String descricao;

    protected EventoHistorico() {}

    EventoHistorico(LocalDateTime em, Status status, Usuario autor, String descricao) {
        this.em = em;
        this.status = status;
        this.autor = autor;
        this.descricao = descricao;
    }

    public LocalDateTime getEm() {
        return em;
    }

    public Status getStatus() {
        return status;
    }

    public Usuario getAutor() {
        return autor;
    }

    public String getDescricao() {
        return descricao;
    }
}
