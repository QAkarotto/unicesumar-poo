
package com.eventos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "inscricoes",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"evento_id", "participante_id"}
    )
)
public class Inscricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @ManyToOne(optional = false)
    @JoinColumn(name = "participante_id", nullable = false)
    private Participante participante;

    @Column(nullable = false)
    private LocalDateTime dataInscricao;

    public Inscricao() {}

    @PrePersist
    public void antesDeSalvar() {
        if (dataInscricao == null) {
            dataInscricao = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Participante getParticipante() {
        return participante;
    }

    public void setParticipante(Participante participante) {
        this.participante = participante;
    }

    public LocalDateTime getDataInscricao() {
        return dataInscricao;
    }
}
