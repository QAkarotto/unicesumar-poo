package br.edu.seugame.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "participantes", uniqueConstraints = {
        @UniqueConstraint(name = "uq_participantes_torneio_id", columnNames = {"torneio_id", "id"}),
        @UniqueConstraint(name = "uq_participantes_nome", columnNames = {"torneio_id", "nome"}),
        @UniqueConstraint(name = "uq_participantes_ordem", columnNames = {"torneio_id", "ordem_sorteio"})
})
public class Participante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "torneio_id", nullable = false)
    private Torneio torneio;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(name = "ordem_sorteio", nullable = false)
    private int ordemSorteio;

    protected Participante() {}

    public Participante(Torneio torneio, String nome, int ordemSorteio) {
        this.torneio = java.util.Objects.requireNonNull(torneio);
        this.nome = validarNome(nome);
        if (ordemSorteio <= 0) throw new IllegalArgumentException("Ordem deve ser positiva.");
        this.ordemSorteio = ordemSorteio;
        torneio.incluirParticipante(this);
    }

    public static String validarNome(String nome) { return DadosDominio.texto(nome, 1, 100, "Participante"); }
    public static java.util.List<String> validarNomes(java.util.List<String> nomes) {
        if (nomes == null || nomes.size() < 3) throw new IllegalArgumentException("Informe pelo menos três participantes.");
        var vistos = new java.util.TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        var normalizados = new java.util.ArrayList<String>();
        for (String nome : nomes) {
            String valido = validarNome(nome);
            if (!vistos.add(valido)) throw new ParticipanteDuplicadoException();
            normalizados.add(valido);
        }
        return java.util.List.copyOf(normalizados);
    }
    public boolean mesmoParticipante(Participante outro) {
        return outro != null && (this == outro || (getId() != null && getId().equals(outro.getId())));
    }
    public void alterarNome(String nome) {
        if (!torneio.podeAlterarParticipantes()) throw new OperacaoNaoPermitidaException("Participantes bloqueados.");
        String novo = validarNome(nome);
        torneio.validarNomeDisponivel(novo, this);
        this.nome = novo;
    }

    public Long getId() { return id; }
    public Torneio getTorneio() { return torneio; }
    public String getNome() { return nome; }
    public int getOrdemSorteio() { return ordemSorteio; }
}
