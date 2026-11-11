package br.edu.sistemaacademico.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "turma")
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", length = 50, nullable = false)
    private String codigo;

    @Embedded
    private PeriodoLetivo periodoLetivo;

    @OneToMany(mappedBy = "turma", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OfertaDisciplina> ofertas = new ArrayList<>();

    protected Turma() {
    }

    public Turma(String codigo, PeriodoLetivo periodoLetivo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("O código da turma é obrigatório.");
        }
        if (periodoLetivo == null) {
            throw new IllegalArgumentException("O período letivo é obrigatório.");
        }

        this.codigo = codigo.trim();
        this.periodoLetivo = periodoLetivo;
    }

    public Turma(String codigo, Disciplina disciplina, PeriodoLetivo periodoLetivo) {
        this(codigo, periodoLetivo);
        ofertarDisciplina(disciplina);
    }

    public Long getId() {
        return id;
    }

    public OfertaDisciplina ofertarDisciplina(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException("A disciplina é obrigatória.");
        }
        boolean disciplinaJaOfertada = ofertas.stream()
                .anyMatch(oferta -> oferta.getDisciplina().equals(disciplina));

        if (disciplinaJaOfertada) {
            throw new IllegalArgumentException(
                    "A disciplina já foi ofertada para esta turma."
            );
        }

        var oferta = new OfertaDisciplina(this, disciplina);
        ofertas.add(oferta);
        return oferta;
    }

    public String getCodigo() {
        return codigo;
    }

    public PeriodoLetivo getPeriodoLetivo() {
        return periodoLetivo;
    }

    public List<OfertaDisciplina> getOfertas() {
        return List.copyOf(ofertas);
    }

    public OfertaDisciplina obterUnicaOferta() {
        if (ofertas.size() != 1) {
            throw new IllegalStateException(
                    "A turma deve possuir uma única oferta para esta operação."
            );
        }
        return ofertas.getFirst();
    }

    @Override
    public String toString() {
        return codigo + " - " + periodoLetivo;
    }
}
