package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Turma {

    private final String codigo;
    private final PeriodoLetivo periodoLetivo;
    private final List<OfertaDisciplina> ofertas;

    public Turma(
            String codigo,
            PeriodoLetivo periodoLetivo
    ) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException(
                    "Código da turma é obrigatório."
            );
        }

        if (periodoLetivo == null) {
            throw new IllegalArgumentException(
                    "Período letivo é obrigatório."
            );
        }

        this.codigo = codigo;
        this.periodoLetivo = periodoLetivo;
        this.ofertas = new ArrayList<>();
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

    public OfertaDisciplina ofertarDisciplina(
            Disciplina disciplina
    ) {
        if (disciplina == null) {
            throw new IllegalArgumentException(
                    "Disciplina é obrigatória."
            );
        }

        if (possuiDisciplina(disciplina)) {
            throw new IllegalStateException(
                    "A disciplina já está ofertada nesta turma."
            );
        }

        OfertaDisciplina oferta =
                new OfertaDisciplina(this, disciplina);

        ofertas.add(oferta);

        return oferta;
    }

    private boolean possuiDisciplina(
            Disciplina disciplina
    ) {
        return ofertas.stream()
                .anyMatch(oferta ->
                        oferta.getDisciplina().equals(disciplina)
                );
    }

    @Override
    public String toString() {
        return codigo + " - " + periodoLetivo;
    }
}