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
        validarTexto(codigo, "Código");

        if (periodoLetivo == null) {
            throw new IllegalArgumentException(
                    "O período letivo não pode ser nulo."
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
                    "A disciplina não pode ser nula."
            );
        }

        if (possuiDisciplina(disciplina)) {
            throw new IllegalArgumentException(
                    "A disciplina já foi ofertada nesta turma."
            );
        }

        OfertaDisciplina oferta = new OfertaDisciplina(
                disciplina,
                this
        );

        ofertas.add(oferta);

        return oferta;
    }

    private boolean possuiDisciplina(Disciplina disciplina) {
        for (OfertaDisciplina oferta : ofertas) {
            if (oferta.getDisciplina().getCodigo()
                    .equals(disciplina.getCodigo())) {

                return true;
            }
        }

        return false;
    }

    private static void validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                    campo + " não pode ser nulo ou vazio."
            );
        }
    }

    @Override
    public String toString() {
        return "Turma{" +
                "codigo='" + codigo + '\'' +
                ", periodoLetivo=" + periodoLetivo +
                ", ofertas=" + ofertas +
                '}';
    }

}
