package br.edu.seugame.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Objects;

@Entity
@Table(name = "classificacao", uniqueConstraints = @UniqueConstraint(
        name = "uq_classificacao_participante", columnNames = {"torneio_id", "participante_id"}))
public class Classificacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "torneio_id", nullable = false)
    private Torneio torneio;

    @Column(name = "participante_id", nullable = false)
    private Long participanteId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns({
            @JoinColumn(name = "torneio_id", referencedColumnName = "torneio_id", insertable = false, updatable = false),
            @JoinColumn(name = "participante_id", referencedColumnName = "id", insertable = false, updatable = false)
    })
    private Participante participante;

    @Column(nullable = false)
    private int jogos;
    @Column(nullable = false)
    private int vitorias;
    @Column(nullable = false)
    private int empates;
    @Column(nullable = false)
    private int derrotas;
    @Column(name = "gols_pro", nullable = false)
    private int golsPro;
    @Column(name = "gols_contra", nullable = false)
    private int golsContra;
    @Column(nullable = false)
    private int saldo;
    @Column(nullable = false)
    private int pontos;

    protected Classificacao() {}

    public Classificacao(Torneio torneio, Participante participante) {
        java.util.Objects.requireNonNull(torneio).exigirParticipante(participante);
        if (torneio.getTipo() != TipoTorneio.TABELA) throw new IllegalArgumentException("Classificação exige formato tabela.");
        this.torneio = torneio;
        this.participante = Objects.requireNonNull(participante);
        this.participanteId = Objects.requireNonNull(
                participante.getId(), "Persista o participante antes de criar sua classificação.");
    }

    void zerarEstatisticas() {
        torneio.exigirEditavel();
        jogos = vitorias = empates = derrotas = golsPro = golsContra = saldo = pontos = 0;
    }

    void contabilizar(int pro, int contra) {
        torneio.exigirEditavel();
        jogos++;
        golsPro += pro;
        golsContra += contra;
        if (pro > contra) { vitorias++; pontos += 3; }
        else if (pro == contra) { empates++; pontos++; }
        else derrotas++;
        saldo = golsPro - golsContra;
    }

    public Long getId() { return id; }
    public Torneio getTorneio() { return torneio; }
    public Participante getParticipante() { return participante; }
    public int getJogos() { return jogos; }
    public int getVitorias() { return vitorias; }
    public int getEmpates() { return empates; }
    public int getDerrotas() { return derrotas; }
    public int getGolsPro() { return golsPro; }
    public int getGolsContra() { return golsContra; }
    public int getSaldo() { return saldo; }
    public int getPontos() { return pontos; }
}
