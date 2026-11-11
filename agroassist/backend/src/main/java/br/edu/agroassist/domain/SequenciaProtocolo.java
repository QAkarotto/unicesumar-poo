package br.edu.agroassist.domain;

import jakarta.persistence.*;

/** Contador único, bloqueado pelo repository durante a abertura de uma solicitação. */
@Entity
@Table(name = "sequencia_protocolo")
public class SequenciaProtocolo {
    @Id private Long id;

    @Column(nullable = false)
    private long ultimo;

    protected SequenciaProtocolo() {}

    public long proximo() {
        return ++ultimo;
    }

    public long getUltimo() {
        return ultimo;
    }
}
