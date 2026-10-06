package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Turma {
    private final String codigo;
    private final PeriodoLetivo periodoLetivo;
    private final List<OfertaDisciplina> ofertas = new ArrayList<>();

    public Turma(String codigo, PeriodoLetivo periodoLetivo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código da turma não pode ser vazio");
        }
        if (periodoLetivo == null) {
            throw new IllegalArgumentException("Período letivo não pode ser nulo");
        }

        this.codigo = codigo;
        this.periodoLetivo = periodoLetivo;
    }

    public String getCodigo() {
        return codigo;
    }

    public PeriodoLetivo getPeriodoLetivo() {
        return periodoLetivo;
    }

    public List<OfertaDisciplina> getOfertas() {
        return Collections.unmodifiableList(ofertas);
    }

    public OfertaDisciplina ofertarDisciplina(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException("Disciplina não pode ser nula");
        }
        if (possuiOferta(disciplina)) {
            throw new IllegalArgumentException("Disciplina já ofertada nesta turma");
        }

        var oferta = new OfertaDisciplina(this, disciplina);
        ofertas.add(oferta);
        return oferta;
    }

    private boolean possuiOferta(Disciplina disciplina) {
        return ofertas.stream().anyMatch(oferta -> oferta.getDisciplina().equals(disciplina));
    }

    @Override
    public String toString() {
        return String.format("Turma(código=%s, período=%s, disciplinas ofertadas=%d)",
                codigo, periodoLetivo, ofertas.size());
    }
}
