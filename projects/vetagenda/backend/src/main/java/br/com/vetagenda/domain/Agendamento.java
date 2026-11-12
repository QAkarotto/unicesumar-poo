package br.com.vetagenda.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "agendamento")
public class Agendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @NotBlank
    @Column(nullable = false, length = 255)
    private String motivo;

    @Column(length = 500)
    private String observacoes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusAgendamento status = StatusAgendamento.AGENDADO;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "veterinario_id", nullable = false)
    private Veterinario veterinario;

    @OneToOne(mappedBy = "agendamento")
    private Atendimento atendimento;

    protected Agendamento() {}

    public Agendamento(LocalDateTime dataHora, String motivo, String observacoes,
                       Animal animal, Veterinario veterinario) {
        this.dataHora = dataHora;
        this.motivo = motivo;
        this.observacoes = observacoes;
        this.animal = animal;
        this.veterinario = veterinario;
        this.status = StatusAgendamento.AGENDADO;
    }

    public Long getId() { return id; }
    public LocalDateTime getDataHora() { return dataHora; }
    public String getMotivo() { return motivo; }
    public String getObservacoes() { return observacoes; }
    public StatusAgendamento getStatus() { return status; }
    public Animal getAnimal() { return animal; }
    public Veterinario getVeterinario() { return veterinario; }
    public Atendimento getAtendimento() { return atendimento; }

    public void atualizar(LocalDateTime dataHora, String motivo, String observacoes,
                          Animal animal, Veterinario veterinario) {
        if (status == StatusAgendamento.CANCELADO || status == StatusAgendamento.REALIZADO) {
            throw new IllegalStateException("Não é possível alterar um agendamento cancelado ou realizado.");
        }
        this.dataHora = dataHora;
        this.motivo = motivo;
        this.observacoes = observacoes;
        this.animal = animal;
        this.veterinario = veterinario;
    }

    public void cancelar() {
        if (status == StatusAgendamento.CANCELADO || status == StatusAgendamento.REALIZADO) {
            throw new IllegalStateException("Somente agendamentos ativos podem ser cancelados.");
        }
        this.status = StatusAgendamento.CANCELADO;
    }

    public void confirmar() {
        if (status != StatusAgendamento.AGENDADO) {
            throw new IllegalStateException("Somente agendamentos novos podem ser confirmados.");
        }
        this.status = StatusAgendamento.CONFIRMADO;
    }

    public void marcarComoRealizado() {
        if (status == StatusAgendamento.CANCELADO) {
            throw new IllegalStateException("Um agendamento cancelado não pode ser realizado.");
        }
        this.status = StatusAgendamento.REALIZADO;
    }
}
