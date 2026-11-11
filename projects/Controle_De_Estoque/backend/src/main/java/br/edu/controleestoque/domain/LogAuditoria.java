package br.edu.controleestoque.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.hibernate.annotations.Immutable;

/**
 * Registro de auditoria de uma ação feita no sistema.
 * Marcada como @Immutable: o Hibernate nunca gera UPDATE para ela, e a API
 * não oferece nenhuma forma de excluí-la. A remoção só ocorre direto no banco.
 */
@Entity
@Immutable
@Table(name = "log_auditoria")
public class LogAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AcaoLog acao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EntidadeLog entidade;

    @Column(name = "entidade_id")
    private Long entidadeId;

    @Column(nullable = false)
    private String descricao;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    protected LogAuditoria() {
    }

    public LogAuditoria(AcaoLog acao, EntidadeLog entidade, Long entidadeId, String descricao) {
        this.acao = acao;
        this.entidade = entidade;
        this.entidadeId = entidadeId;
        this.descricao = descricao;
        this.dataHora = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public AcaoLog getAcao() {
        return acao;
    }

    public EntidadeLog getEntidade() {
        return entidade;
    }

    public Long getEntidadeId() {
        return entidadeId;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }
}
