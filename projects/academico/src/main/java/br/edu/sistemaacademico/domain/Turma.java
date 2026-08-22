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
            throw new IllegalArgumentException(
                    "Código da turma é obrigatório."
            );
        }

        if (periodoLetivo == null) {
            throw new IllegalArgumentException(
                    "Período letivo é obrigatório."
            );
        }

        this.codigo = codigo.trim();
        this.periodoLetivo = periodoLetivo;
    }

    public String getCodigo() {
        return codigo;
    }

    public PeriodoLetivo getPeriodoLetivo() {
        return periodoLetivo;
    }

    public OfertaDisciplina ofertarDisciplina(Disciplina disciplina) {

        if (disciplina == null) {
            throw new IllegalArgumentException(
                    "Disciplina é obrigatória."
            );
        }

        for (OfertaDisciplina oferta : ofertas) {
            if (oferta.getDisciplina().getCodigo()
                    .equals(disciplina.getCodigo())) {

                throw new IllegalArgumentException(
                        "Disciplina já ofertada nesta turma."
                );
            }
        }

        OfertaDisciplina oferta =
                new OfertaDisciplina(disciplina, this);

        ofertas.add(oferta);

        return oferta;
    }

    public List<OfertaDisciplina> getOfertas() {
        return Collections.unmodifiableList(ofertas);
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