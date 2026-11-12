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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import jakarta.persistence.OneToMany;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.generator.EventType;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "torneios")
public class Torneio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario criador;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, length = 2000)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, length = 11)
    private TipoTorneio tipo;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, length = 10)
    private StatusTorneio status;

    @Column(name = "campeao_id")
    private Long campeaoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "id", referencedColumnName = "torneio_id", insertable = false, updatable = false),
            @JoinColumn(name = "campeao_id", referencedColumnName = "id", insertable = false, updatable = false)
    })
    private Participante campeao;

    @Generated(event = EventType.INSERT)
    @JdbcTypeCode(SqlTypes.LOCAL_DATE_TIME)
    @Column(name = "criado_em", nullable = false, insertable = false, updatable = false)
    private LocalDateTime criadoEm;

    @OneToMany(mappedBy = "torneio", fetch = FetchType.LAZY)
    private List<Participante> participantes = new ArrayList<>();

    @OneToMany(mappedBy = "torneio", fetch = FetchType.LAZY)
    private List<Partida> partidas = new ArrayList<>();

    protected Torneio() {}

    public Torneio(Usuario criador, String nome, String descricao, TipoTorneio tipo,
            LocalDate dataInicio, Clock relogio) {
        this.criador = Objects.requireNonNull(criador, "Criador obrigatório.");
        this.tipo = Objects.requireNonNull(tipo, "Formato obrigatório.");
        this.status = StatusTorneio.FUTURO;
        alterarDadosBasicos(nome, descricao, dataInicio, relogio);
    }

    public void atualizarEstado(Clock relogio) {
        Objects.requireNonNull(relogio, "Relógio obrigatório.");
        if (status == StatusTorneio.FUTURO && (!dataInicio.isAfter(LocalDate.now(relogio)) || temResultados())) iniciar();
    }

    public StatusTorneio estadoEm(Clock relogio) {
        return status == StatusTorneio.FUTURO && (!dataInicio.isAfter(LocalDate.now(relogio)) || temResultados())
                ? StatusTorneio.ANDAMENTO : status;
    }

    public void prepararReconstrucao(boolean substituirParticipantes) {
        exigirEditavel();
        if (!podeAlterarParticipantes()) throw new OperacaoNaoPermitidaException("Resultados registrados impedem reconstrução estrutural.");
        partidas.clear();
        if (substituirParticipantes) participantes.clear();
    }

    public void desvincularParaExclusao() {
        validarExclusao();
        partidas.clear(); participantes.clear();
    }

    public boolean temResultados() { return partidas.stream().anyMatch(Partida::temResultado); }
    public boolean podeEditarFormato() { return status == StatusTorneio.FUTURO && !temResultados(); }
    public boolean podeEditarFormato(Clock relogio) { atualizarEstado(relogio); return podeEditarFormato(); }
    public boolean podeAlterarParticipantes() { return status != StatusTorneio.FINALIZADO && !temResultados(); }
    public void exigirEditavel() {
        if (status == StatusTorneio.FINALIZADO) throw new OperacaoNaoPermitidaException("Torneio finalizado não pode ser alterado.");
    }
    public void validarExclusao() { exigirEditavel(); }

    public void alterarDadosBasicos(String nome, String descricao, LocalDate dataInicio, Clock relogio) {
        exigirEditavel();
        String novoNome = DadosDominio.texto(nome, 3, 120, "Nome do torneio");
        String novaDescricao = DadosDominio.texto(descricao, 0, 2000, "Descrição");
        Objects.requireNonNull(dataInicio, "Data obrigatória.");
        Objects.requireNonNull(relogio, "Relógio obrigatório.");
        if (this.dataInicio != null) atualizarEstado(relogio);
        this.nome = novoNome;
        this.descricao = novaDescricao;
        this.dataInicio = dataInicio;
        atualizarEstado(relogio);
    }

    public void alterarFormato(TipoTorneio formato, Clock relogio) {
        Objects.requireNonNull(formato, "Formato obrigatório.");
        atualizarEstado(relogio);
        if (!podeEditarFormato()) throw new OperacaoNaoPermitidaException("Formato bloqueado após início ou resultado.");
        this.tipo = formato;
    }

    public void iniciar() {
        exigirEditavel();
        status = StatusTorneio.ANDAMENTO;
    }

    public boolean mesmoTorneio(Torneio outro) {
        return outro != null && (this == outro || (getId() != null && getId().equals(outro.getId())));
    }

    public void exigirParticipante(Participante participante) {
        if (participante == null || !mesmoTorneio(participante.getTorneio()))
            throw new IllegalArgumentException("Participante não pertence a este torneio.");
    }

    void incluirParticipante(Participante participante) {
        if (!podeAlterarParticipantes()) throw new OperacaoNaoPermitidaException("Participantes bloqueados.");
        exigirParticipante(participante);
        validarNomeDisponivel(participante.getNome(), null);
        if (participantes.stream().anyMatch(p -> p.getOrdemSorteio() == participante.getOrdemSorteio()))
            throw new IllegalArgumentException("Ordem de sorteio repetida.");
        participantes.add(participante);
    }

    void validarNomeDisponivel(String nome, Participante atual) {
        if (participantes.stream().anyMatch(p -> !p.mesmoParticipante(atual) && p.getNome().equalsIgnoreCase(nome)))
            throw new ParticipanteDuplicadoException();
    }

    public void validarParticipantes() {
        if (participantes.size() < 3) throw new OperacaoNaoPermitidaException("Informe pelo menos três participantes.");
        var nomes = new HashSet<String>();
        for (Participante p : participantes) {
            exigirParticipante(p);
            if (!nomes.add(Participante.validarNome(p.getNome()).toLowerCase(Locale.ROOT)))
                throw new ParticipanteDuplicadoException();
        }
    }

    public void removerParticipante(Participante participante) {
        if (!podeAlterarParticipantes()) throw new OperacaoNaoPermitidaException("Participantes bloqueados.");
        exigirParticipante(participante);
        if (participantes.size() <= 3) throw new OperacaoNaoPermitidaException("Mínimo de três participantes.");
        if (partidas.stream().anyMatch(p -> p.referencia(participante)))
            throw new OperacaoNaoPermitidaException("Remova a estrutura dependente antes de alterar participantes.");
        if (!participantes.removeIf(p -> p.mesmoParticipante(participante)))
            throw new IllegalArgumentException("Participante não está na lista do torneio.");
    }

    void incluirPartida(Partida partida) {
        exigirEditavel();
        if (partidas.stream().anyMatch(p -> p.getRodada() == partida.getRodada() && p.getOrdem() == partida.getOrdem() && p.getFase() == partida.getFase()))
            throw new IllegalArgumentException("Posição de partida repetida.");
        partidas.add(partida);
    }

    public void finalizar(Participante campeaoDefinido) {
        exigirEditavel();
        validarParticipantes();
        exigirParticipante(campeaoDefinido);
        if (campeaoDefinido.getId() == null || !participantes.stream().anyMatch(p -> p.mesmoParticipante(campeaoDefinido)))
            throw new IllegalArgumentException("Campeão deve ser um participante persistido da competição.");
        if (partidas.isEmpty() || partidas.stream().anyMatch(p -> !p.estaResolvida()))
            throw new OperacaoNaoPermitidaException("Registre todos os resultados antes de finalizar.");
        if (tipo == TipoTorneio.TABELA && partidas.size() != (long) participantes.size() * (participantes.size() - 1) / 2)
            throw new OperacaoNaoPermitidaException("Tabela ainda incompleta.");
        if (tipo == TipoTorneio.TABELA) {
            var pares = new HashSet<String>();
            for (Partida p : partidas) {
                long a = p.getParticipante1().getId(), b = p.getParticipante2().getId();
                if (!pares.add(Math.min(a,b) + ":" + Math.max(a,b))) throw new OperacaoNaoPermitidaException("Confrontos repetidos na tabela.");
            }
        }
        if (tipo == TipoTorneio.CHAVEAMENTO) {
            if (partidas.stream().filter(Partida::temResultado).count() != participantes.size() - 1L)
                throw new OperacaoNaoPermitidaException("Eliminatória ainda incompleta.");
            int ultima = partidas.stream().mapToInt(Partida::getRodada).max().orElseThrow();
            var finais = partidas.stream().filter(p -> p.getRodada() == ultima).toList();
            if (finais.size() != 1 || !campeaoDefinido.mesmoParticipante(finais.getFirst().getVencedor()))
                throw new IllegalArgumentException("Campeão deve ser o vencedor da final.");
        }
        if (tipo == TipoTorneio.TABELA && !campeaoDefinido.mesmoParticipante(new ClassificadorTabela().determinarCampeao(this)))
            throw new IllegalArgumentException("Campeão deve ser o primeiro colocado pelo desempate oficial.");
        if (tipo == TipoTorneio.CHAVEAMENTO && !campeaoDefinido.mesmoParticipante(new ReconstrutorChaveamento().determinarCampeao(this)))
            throw new IllegalArgumentException("Campeão incompatível com a chave reconstruída.");
        this.campeao = campeaoDefinido;
        this.campeaoId = campeaoDefinido.getId();
        this.status = StatusTorneio.FINALIZADO;
    }

    public List<Participante> getParticipantes() { return Collections.unmodifiableList(participantes); }
    public void finalizar() {
        exigirEditavel();
        finalizar(tipo == TipoTorneio.TABELA ? new ClassificadorTabela().determinarCampeao(this)
                : new ReconstrutorChaveamento().determinarCampeao(this));
    }
    public List<Partida> getPartidas() { return Collections.unmodifiableList(partidas); }

    public Long getId() { return id; }
    public Usuario getCriador() { return criador; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public TipoTorneio getTipo() { return tipo; }
    public LocalDate getDataInicio() { return dataInicio; }
    public StatusTorneio getStatus() { return status; }
    public Participante getCampeao() { return campeao; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
}
