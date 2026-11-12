package br.edu.seugame.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Objects;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "partidas", uniqueConstraints = @UniqueConstraint(
        name = "uq_partidas_posicao", columnNames = {"torneio_id", "fase", "rodada", "ordem"}))
public class Partida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "torneio_id", nullable = false)
    private Torneio torneio;

    @Column(name = "participante1_id")
    private Long participante1Id;

    @Column(name = "participante2_id")
    private Long participante2Id;

    @Column(name = "vencedor_id")
    private Long vencedorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "torneio_id", referencedColumnName = "torneio_id", insertable = false, updatable = false),
            @JoinColumn(name = "participante1_id", referencedColumnName = "id", insertable = false, updatable = false)
    })
    private Participante participante1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "torneio_id", referencedColumnName = "torneio_id", insertable = false, updatable = false),
            @JoinColumn(name = "participante2_id", referencedColumnName = "id", insertable = false, updatable = false)
    })
    private Participante participante2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "torneio_id", referencedColumnName = "torneio_id", insertable = false, updatable = false),
            @JoinColumn(name = "vencedor_id", referencedColumnName = "id", insertable = false, updatable = false)
    })
    private Participante vencedor;

    @Column(nullable = false)
    private int rodada;

    @Column(nullable = false)
    private int ordem;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, length = 5)
    private FasePartida fase;

    @Column(name = "placar1")
    private Short placar1;

    @Column(name = "placar2")
    private Short placar2;

    protected Partida() {}

    public Partida(Torneio torneio, Participante participante1, Participante participante2,
            int rodada, int ordem, FasePartida fase) {
        this(torneio, participante1, participante2, rodada, ordem, fase, null, null, null);
    }

    public Partida(Torneio torneio, Participante participante1, Participante participante2,
            int rodada, int ordem, FasePartida fase, Short placar1, Short placar2, Participante vencedor) {
        this.torneio = torneio;
        Objects.requireNonNull(torneio).exigirEditavel();
        Objects.requireNonNull(fase, "Fase obrigatória.");
        if ((torneio.getTipo() == TipoTorneio.TABELA) != (fase == FasePartida.LIGA)) throw new IllegalArgumentException("Fase incompatível com formato.");
        if (rodada <= 0 || ordem <= 0) throw new IllegalArgumentException("Posição deve ser positiva.");
        if (participante1 != null) torneio.exigirParticipante(participante1);
        if (participante2 != null) torneio.exigirParticipante(participante2);
        if (participante1 != null && participante1.mesmoParticipante(participante2)) throw new IllegalArgumentException("Autoconfronto inválido.");
        if ((fase == FasePartida.LIGA && (participante1 == null || participante2 == null))
                || (rodada == 1 && participante1 == null && participante2 == null)) throw new IllegalArgumentException("Confronto inicial inválido.");
        this.participante1 = participante1;
        this.participante2 = participante2;
        this.vencedor = vencedor;
        this.participante1Id = idPersistido(participante1);
        this.participante2Id = idPersistido(participante2);
        this.vencedorId = idPersistido(vencedor);
        this.rodada = rodada;
        this.ordem = ordem;
        this.fase = fase;
        if (placar1 != null || placar2 != null) {
            if (placar1 == null || placar2 == null) throw new IllegalArgumentException("Informe ambos os placares.");
            validarResultado(placar1, placar2);
            Participante esperado = placar1 > placar2 ? participante1 : placar2 > placar1 ? participante2 : null;
            if (esperado == null ? vencedor != null : !esperado.mesmoParticipante(vencedor)) throw new IllegalArgumentException("Vencedor incompatível.");
            this.placar1 = placar1;
            this.placar2 = placar2;
            torneio.iniciar();
        } else if (vencedor != null) {
            if (fase != FasePartida.CHAVE || rodada != 1 || (participante1 == null) == (participante2 == null)
                    || !vencedor.mesmoParticipante(participante1 != null ? participante1 : participante2))
                throw new IllegalArgumentException("Vencedor sem resultado só é permitido em BYE inicial.");
        }
        torneio.incluirPartida(this);
    }

    private void validarResultado(int primeiro, int segundo) {
        torneio.exigirEditavel();
        if ((torneio.getTipo() == TipoTorneio.TABELA) != (fase == FasePartida.LIGA))
            throw new OperacaoNaoPermitidaException("Reconstrua a competição após alterar seu formato.");
        if (participante1 == null || participante2 == null) throw new OperacaoNaoPermitidaException("Confronto ainda indisponível.");
        if (primeiro < 0 || primeiro > 999 || segundo < 0 || segundo > 999) throw new IllegalArgumentException("Placares devem estar entre 0 e 999.");
        if (fase == FasePartida.CHAVE && primeiro == segundo) throw new IllegalArgumentException("Eliminatória não permite empate.");
    }
    public void registrarResultado(int primeiro, int segundo) {
        validarResultado(primeiro, segundo);
        Participante novo = primeiro > segundo ? participante1 : segundo > primeiro ? participante2 : null;
        Long novoId = idPersistido(novo);
        this.placar1 = (short) primeiro;
        this.placar2 = (short) segundo;
        this.vencedor = novo;
        this.vencedorId = novoId;
        torneio.iniciar();
        if (fase == FasePartida.CHAVE) new ReconstrutorChaveamento().reconstruir(torneio);
    }

    void recomporConfronto(Participante primeiro, Participante segundo) {
        torneio.exigirEditavel();
        if (fase != FasePartida.CHAVE) throw new OperacaoNaoPermitidaException("Somente chaveamento pode recompor vagas.");
        if (primeiro != null) torneio.exigirParticipante(primeiro);
        if (segundo != null) torneio.exigirParticipante(segundo);
        if (primeiro != null && primeiro.mesmoParticipante(segundo)) throw new IllegalArgumentException("Autoconfronto inválido.");
        boolean preservar = primeiro != null && segundo != null && primeiro.mesmoParticipante(participante1)
                && segundo.mesmoParticipante(participante2);
        Long primeiroId = idPersistido(primeiro), segundoId = idPersistido(segundo);
        participante1 = primeiro; participante2 = segundo;
        participante1Id = primeiroId; participante2Id = segundoId;
        if (!preservar) { placar1 = null; placar2 = null; }
        vencedor = null;
        if (temResultado()) vencedor = placar1 > placar2 ? primeiro : segundo;
        else if (rodada == 1 && (primeiro == null) != (segundo == null)) vencedor = primeiro != null ? primeiro : segundo;
        vencedorId = idPersistido(vencedor);
    }
    public boolean temResultado() { return placar1 != null && placar2 != null; }
    public boolean estaResolvida() { return temResultado() || (fase == FasePartida.CHAVE && rodada == 1 && vencedor != null); }
    public boolean referencia(Participante p) {
        return p.mesmoParticipante(participante1) || p.mesmoParticipante(participante2) || p.mesmoParticipante(vencedor);
    }

    private static Long idPersistido(Participante participante) {
        return participante == null ? null : Objects.requireNonNull(
                participante.getId(), "Persista o participante antes de referenciá-lo na partida.");
    }

    public Long getId() { return id; }
    public Torneio getTorneio() { return torneio; }
    public Participante getParticipante1() { return participante1; }
    public Participante getParticipante2() { return participante2; }
    public Participante getVencedor() { return vencedor; }
    public int getRodada() { return rodada; }
    public int getOrdem() { return ordem; }
    public FasePartida getFase() { return fase; }
    public Short getPlacar1() { return placar1; }
    public Short getPlacar2() { return placar2; }
}
