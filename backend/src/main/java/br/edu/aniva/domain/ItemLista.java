package br.edu.aniva.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// Relação de uma pessoa com uma obra na lista pessoal. Uma mesma linha guarda se a obra é favorita e se está na pauta
@Entity
@Table(name = "item_lista")
public class ItemLista {

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
    private boolean favorito;

    /** null = a obra não está na pauta. */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StatusPauta status;

    @Column(nullable = false)
    private int progresso;

    protected ItemLista() {
        // exigido pelo JPA
    }

    public ItemLista(Usuario usuario, Obra obra) {
        this.usuario = usuario;
        this.obra = obra;
    }

    public void alternarFavorito() {
        favorito = !favorito;
    }

    // Entra na pauta como "a fazer" ou sai dela.
    public void alternarPauta() {
        if (naPauta()) {
            status = null;
            progresso = 0;
        } else {
            status = StatusPauta.A_FAZER;
            progresso = 0;
        }
    }

    /**
     * Regras da pauta:
     * - progresso vai de 0 a 100;
     * - "concluido" sempre fica com 100% e "a-fazer" sempre com 0%;
     * - alterar a pauta de uma obra que ainda não está nela a adiciona.
     */
    public void atualizarPauta(StatusPauta novoStatus, Integer novoProgresso) {
        if (novoStatus == null && novoProgresso == null) {
            throw new IllegalArgumentException("Informe a situação ou o progresso.");
        }
        if (novoProgresso != null && (novoProgresso < 0 || novoProgresso > 100)) {
            throw new IllegalArgumentException("O progresso deve estar entre 0 e 100.");
        }

        if (novoStatus != null) {
            status = novoStatus;
        } else if (status == null) {
            status = StatusPauta.A_FAZER;
        }
        if (novoProgresso != null) {
            progresso = novoProgresso;
        }

        if (status == StatusPauta.CONCLUIDO) {
            progresso = 100;
        } else if (status == StatusPauta.A_FAZER) {
            progresso = 0;
        }
    }

    public boolean naPauta() {
        return status != null;
    }

    // Sem favorito e fora da pauta: a linha não tem mais utilidade.
    public boolean estaVazio() {
        return !favorito && status == null;
    }

    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public Obra getObra() { return obra; }
    public boolean isFavorito() { return favorito; }
    public StatusPauta getStatus() { return status; }
    public int getProgresso() { return progresso; }
}
