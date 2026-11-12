package br.edu.aniva.domain;

import br.edu.aniva.exception.OperacaoNaoPermitidaException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Comentário de uma pessoa em uma obra. Outras pessoas podem marcá-lo como útil.
@Entity
@Table(name = "comentario")
public class Comentario {

    private static final int TAMANHO_MAXIMO = 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "obra_id", nullable = false)
    private Obra obra;

    @ManyToOne(optional = false)
    @JoinColumn(name = "autor_id", nullable = false)
    private Usuario autor;

    @Column(nullable = false, length = TAMANHO_MAXIMO)
    private String texto;

    @Column(nullable = false)
    private boolean spoiler;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    // Relação N:N: um comentário tem várias curtidas e uma pessoa curte vários comentários.
    @ManyToMany
    @JoinTable(name = "comentario_curtida",
            joinColumns = @JoinColumn(name = "comentario_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id"))
    private Set<Usuario> curtidas = new HashSet<>();

    protected Comentario() {
        // exigido pelo JPA
    }

    public Comentario(Obra obra, Usuario autor, String texto, boolean spoiler) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("Escreva algo antes de publicar.");
        }
        if (texto.trim().length() > TAMANHO_MAXIMO) {
            throw new IllegalArgumentException("O comentário pode ter no máximo " + TAMANHO_MAXIMO + " caracteres.");
        }
        this.obra = obra;
        this.autor = autor;
        this.texto = texto.trim();
        this.spoiler = spoiler;
        this.criadoEm = LocalDateTime.now();
    }

    // Curte se ainda não curtiu, remove a curtida se já tinha curtido.
    public void alternarCurtida(Usuario usuario) {
        boolean removeu = curtidas.removeIf(curtiu -> curtiu.getId().equals(usuario.getId()));
        if (!removeu) {
            curtidas.add(usuario);
        }
    }

    // Só quem escreveu o comentário pode apagá-lo.
    public void exigirAutor(Usuario usuario) {
        if (!autor.getId().equals(usuario.getId())) {
            throw new OperacaoNaoPermitidaException("Você só pode apagar os seus próprios comentários.");
        }
    }

    public List<Long> idsDeQuemCurtiu() {
        return curtidas.stream().map(Usuario::getId).toList();
    }

    public Long getId() { return id; }
    public Obra getObra() { return obra; }
    public Usuario getAutor() { return autor; }
    public String getTexto() { return texto; }
    public boolean isSpoiler() { return spoiler; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
}
