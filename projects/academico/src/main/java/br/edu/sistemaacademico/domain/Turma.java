package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;

public final class Turma {
    private final String codigo;
    private final PeriodoLetivo periodoLetivo;
    private final List<OfertaDisciplina> ofertas = new ArrayList<>();

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

    OfertaDisciplina obterUnicaOferta() {
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
