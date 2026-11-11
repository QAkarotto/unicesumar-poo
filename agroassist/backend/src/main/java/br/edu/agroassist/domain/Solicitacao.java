package br.edu.agroassist.domain;

import br.edu.agroassist.exception.DadosInvalidosException;
import br.edu.agroassist.exception.OperacaoNaoPermitidaException;
import br.edu.agroassist.exception.RegraNegocioException;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "solicitacoes")
public class Solicitacao {
    @Id
    @Column(length = 40)
    private String id;

    @Column(nullable = false, unique = true, length = 40)
    private String protocolo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produtor_id", nullable = false)
    private Usuario produtor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "propriedade_id", nullable = false)
    private Propriedade propriedade;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cultura_id", nullable = false)
    private Cultura cultura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tecnico_id")
    private Tecnico tecnico;

    @Column(nullable = false, length = 120)
    private String estadio;

    @Column(nullable = false, length = 4000)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, length = 30)
    private Status status;

    @Column(name = "criada_em", nullable = false)
    private LocalDateTime criadaEm;

    @ElementCollection
    @CollectionTable(name = "solicitacao_fotos", joinColumns = @JoinColumn(name = "solicitacao_id"))
    @OrderColumn(name = "ordem")
    @Column(name = "conteudo", nullable = false, columnDefinition = "LONGTEXT")
    private List<String> fotos = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "historico_solicitacao", joinColumns = @JoinColumn(name = "solicitacao_id"))
    @OrderColumn(name = "ordem")
    private List<EventoHistorico> historico = new ArrayList<>();

    protected Solicitacao() {}

    public Solicitacao(
            String id,
            String protocolo,
            Usuario produtor,
            Propriedade propriedade,
            Cultura cultura,
            String estadio,
            String descricao,
            List<String> fotos,
            LocalDateTime em) {
        this.id = Objects.requireNonNull(id);
        this.protocolo = Validacao.obrigatorio(protocolo, 40, "o protocolo");
        this.produtor = Objects.requireNonNull(produtor);
        produtor.exigirPerfil(Perfil.PRODUTOR);
        this.propriedade = Objects.requireNonNull(propriedade);
        if (!propriedade.getProdutor().getId().equals(produtor.getId()))
            throw new OperacaoNaoPermitidaException("Escolha uma propriedade cadastrada na sua conta.");
        propriedade.exigirCultura(cultura);
        this.cultura = cultura;
        this.estadio = Validacao.texto(estadio, 120, "Estádio da cultura");
        this.descricao = Validacao.obrigatorio(descricao, 4000, "a descrição do problema");
        if (fotos == null || fotos.size() > 3) throw new DadosInvalidosException("Envie no máximo 3 fotos.");
        for (String foto : fotos) {
            if (foto == null
                    || foto.length() > 1_000_000
                    || !(foto.startsWith("data:image/jpeg;base64,")
                            || foto.startsWith("data:image/png;base64,")
                            || foto.startsWith("data:image/svg+xml;charset=utf-8,")))
                throw new DadosInvalidosException(
                        "Envie imagens JPG/PNG reduzidas ou use uma imagem de exemplo (até 1 MB por imagem).");
        }
        this.fotos.addAll(fotos);
        this.criadaEm = Objects.requireNonNull(em);
        registrar(Status.ABERTA, produtor, "Solicitação aberta pelo produtor", em);
    }

    public void atribuir(Tecnico tecnico, Usuario autor, LocalDateTime em) {
        autor.exigirPerfil(Perfil.ADMIN);
        if (status != Status.ABERTA && status != Status.ATRIBUIDA)
            throw new RegraNegocioException(
                    "RN02", "Não é possível reatribuir depois que o atendimento começa.");
        tecnico.exigirHabilitacao(cultura);
        boolean reatribuicao = status == Status.ATRIBUIDA;
        this.tecnico = tecnico;
        registrar(
                Status.ATRIBUIDA,
                autor,
                (reatribuicao ? "Reatribuída a " : "Atribuída a ") + tecnico.getUsuario().getNome(),
                em);
    }

    public void iniciar(Usuario autor, LocalDateTime em) {
        exigirResponsavel(autor);
        if (status != Status.ATRIBUIDA)
            throw new RegraNegocioException("RN03", "Só uma solicitação atribuída pode ser iniciada.");
        registrar(Status.EM_ATENDIMENTO, autor, "Atendimento iniciado", em);
    }

    public Atendimento concluir(
            Usuario autor,
            LocalDate data,
            Modalidade modalidade,
            String diagnostico,
            String recomendacao,
            LocalDateTime em) {
        exigirResponsavel(autor);
        if (status != Status.EM_ATENDIMENTO)
            throw new RegraNegocioException(
                    "RN03",
                    "Inicie o atendimento antes de registrar. Um atendimento concluído não pode ser repetido.");
        Atendimento atendimento =
                new Atendimento(
                        "a" + UUID.randomUUID(), this, tecnico, data, modalidade, diagnostico, recomendacao);
        registrar(Status.CONCLUIDA, autor, "Atendimento registrado", em);
        return atendimento;
    }

    public Avaliacao avaliar(Usuario autor, int nota, String comentario, LocalDateTime em) {
        if (!produtor.getId().equals(autor.getId()))
            throw new OperacaoNaoPermitidaException("Só o produtor que abriu a solicitação pode avaliá-la.");
        if (status != Status.CONCLUIDA)
            throw new RegraNegocioException(
                    "RN04", "A avaliação só é permitida após a conclusão, uma única vez.");
        Avaliacao avaliacao = new Avaliacao("v" + UUID.randomUUID(), this, nota, comentario, em);
        registrar(Status.AVALIADA, autor, "Avaliação enviada (" + nota + " de 5)", em);
        return avaliacao;
    }

    private void exigirResponsavel(Usuario autor) {
        if (tecnico == null || !tecnico.getUsuario().getId().equals(autor.getId()))
            throw new OperacaoNaoPermitidaException(
                    "Só o técnico responsável pode iniciar ou registrar este atendimento.");
    }

    private void registrar(Status novoStatus, Usuario autor, String descricao, LocalDateTime em) {
        status = novoStatus;
        historico.add(new EventoHistorico(Objects.requireNonNull(em), novoStatus, autor, descricao));
    }

    public String getId() {
        return id;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public Usuario getProdutor() {
        return produtor;
    }

    public Propriedade getPropriedade() {
        return propriedade;
    }

    public Cultura getCultura() {
        return cultura;
    }

    public Tecnico getTecnico() {
        return tecnico;
    }

    public String getEstadio() {
        return estadio;
    }

    public String getDescricao() {
        return descricao;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getCriadaEm() {
        return criadaEm;
    }

    public List<String> getFotos() {
        return List.copyOf(fotos);
    }

    public List<EventoHistorico> getHistorico() {
        return List.copyOf(historico);
    }
}
