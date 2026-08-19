package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;

public class Turma {

    private final String codigo;
    private final PeriodoLetivo periodoLetivo;
    private final List<OfertaDisciplina> ofertas;

    public Turma(String codigo, PeriodoLetivo periodoLetivo) {

        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException(
                    "O código da turma é obrigatório."
            );
        }

        if (periodoLetivo == null) {
            throw new IllegalArgumentException(
                    "O período letivo é obrigatório."
            );
        }

        this.codigo = codigo;
        this.periodoLetivo = periodoLetivo;
        this.ofertas = new ArrayList<>();
    }

    public OfertaDisciplina ofertarDisciplina(Disciplina disciplina) {

        if (disciplina == null) {
            throw new IllegalArgumentException(
                    "A disciplina é obrigatória."
            );
        }

        for (OfertaDisciplina oferta : ofertas) {
            if (oferta.getDisciplina().getCodigo()
                    .equals(disciplina.getCodigo())) {

                throw new IllegalArgumentException(
                        "Esta disciplina já foi ofertada nesta turma."
                );
            }
        }

        OfertaDisciplina novaOferta =
                new OfertaDisciplina(this, disciplina);

        ofertas.add(novaOferta);

        return novaOferta;
    }

    public String getCodigo() {
        return codigo;
    }

    public PeriodoLetivo getPeriodoLetivo() {
        return periodoLetivo;
    }

    public List<OfertaDisciplina> getOfertas() {
        return ofertas;
    }
}