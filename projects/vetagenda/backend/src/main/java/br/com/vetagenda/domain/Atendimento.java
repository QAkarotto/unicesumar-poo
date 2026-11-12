package br.com.vetagenda.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "atendimento")
public class Atendimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String diagnostico;

    @Column(columnDefinition = "TEXT")
    private String procedimento;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "agendamento_id", nullable = false, unique = true)
    private Agendamento agendamento;

    protected Atendimento() {}

    public Atendimento(String diagnostico, String procedimento, String observacoes,
                       Agendamento agendamento) {
        this.diagnostico = diagnostico;
        this.procedimento = procedimento;
        this.observacoes = observacoes;
        this.agendamento = agendamento;
    }

    public Long getId() { return id; }
    public String getDiagnostico() { return diagnostico; }
    public String getProcedimento() { return procedimento; }
    public String getObservacoes() { return observacoes; }
    public Agendamento getAgendamento() { return agendamento; }
}
