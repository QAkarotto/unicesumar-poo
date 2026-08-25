package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Turma {

    private final String codigo;
    private final PeriodoLetivo periodoLetivo;

    private final List<OfertaDisciplina> ofertas =
            new ArrayList<>();

    public Turma(
            String codigo,
            PeriodoLetivo periodoLetivo
    ) {
        this.codigo = validarObrigatorio(codigo, "Código");

        if (periodoLetivo == null) {
            throw new IllegalArgumentException(
                    "O período letivo não pode ser nulo."
            );
        }

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

    public OfertaDisciplina ofertarDisciplina(
            Disciplina disciplina
    ) {
        if (disciplina == null) {
            throw new IllegalArgumentException(
                    "A disciplina não pode ser nula."
            );
        }

        for (OfertaDisciplina oferta : ofertas) {
            if (oferta.getDisciplina().equals(disciplina)) {
                throw new IllegalStateException(
                        "A disciplina já foi ofertada nesta turma."
                );
            }
        }

        OfertaDisciplina oferta =
                new OfertaDisciplina(disciplina, this);

        ofertas.add(oferta);

        return oferta;
    }

    private String validarObrigatorio(
            String valor,
            String campo
    ) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                    campo + " não pode ser vazio."
            );
        }

        return valor.trim();
    }

    @Override
    public String toString() {
        return codigo + " - " + periodoLetivo;
    }
}