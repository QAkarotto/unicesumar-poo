package br.edu.agroassist.domain;

import br.edu.agroassist.exception.DadosInvalidosException;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDate;

@Entity
@Table(name = "atendimentos")
public class Atendimento {
    @Id
    @Column(length = 40)
    private String id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "solicitacao_id", nullable = false, unique = true)
    private Solicitacao solicitacao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tecnico_id", nullable = false)
    private Tecnico tecnico;

    @Column(nullable = false)
    private LocalDate data;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, length = 20)
    private Modalidade modalidade;

    @Column(nullable = false, length = 4000)
    private String diagnostico;

    @Column(nullable = false, length = 4000)
    private String recomendacao;

    protected Atendimento() {}

    Atendimento(
            String id,
            Solicitacao solicitacao,
            Tecnico tecnico,
            LocalDate data,
            Modalidade modalidade,
            String diagnostico,
            String recomendacao) {
        if (data == null || modalidade == null)
            throw new DadosInvalidosException("Informe a data e a modalidade do atendimento.");
        this.id = id;
        this.solicitacao = solicitacao;
        this.tecnico = tecnico;
        this.data = data;
        this.modalidade = modalidade;
        this.diagnostico = Validacao.obrigatorio(diagnostico, 4000, "o diagnóstico");
        this.recomendacao = Validacao.obrigatorio(recomendacao, 4000, "a recomendação");
    }

    public String getId() {
        return id;
    }

    public Solicitacao getSolicitacao() {
        return solicitacao;
    }

    public Tecnico getTecnico() {
        return tecnico;
    }

    public LocalDate getData() {
        return data;
    }

    public Modalidade getModalidade() {
        return modalidade;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public String getRecomendacao() {
        return recomendacao;
    }
}
