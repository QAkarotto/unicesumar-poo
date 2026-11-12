package br.edu.aniva.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// Nota de 1 a 10 que uma pessoa deu a uma obra.
@Entity
@Table(name = "avaliacao")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "obra_id", nullable = false)
    private Obra obra;

    @Column(nullable = false)
    private int nota;

    protected Avaliacao() {
        // exigido pelo JPA
    }

    public Avaliacao(Usuario usuario, Obra obra, int nota) {
        this.usuario = usuario;
        this.obra = obra;
        alterarNota(nota);
    }

    public void alterarNota(int novaNota) {
        if (novaNota < 1 || novaNota > 10) {
            throw new IllegalArgumentException("A nota deve ser um número inteiro de 1 a 10.");
        }
        this.nota = novaNota;
    }

    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public Obra getObra() { return obra; }
    public int getNota() { return nota; }
}
