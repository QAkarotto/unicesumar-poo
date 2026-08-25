package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;

public class Turma {

    private String codigo;
    private PeriodoLetivo periodoLetivo;
    private List<OfertaDisciplina> ofertas;

    public Turma(String codigo, PeriodoLetivo periodoLetivo) {

        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código da turma é obrigatório.");
        }

        if (periodoLetivo == null) {
            throw new IllegalArgumentException("Período letivo é obrigatório.");
        }

        this.codigo = codigo;
        this.periodoLetivo = periodoLetivo;
        this.ofertas = new ArrayList<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public OfertaDisciplina ofertarDisciplina(Disciplina disciplina) {

        if (disciplina == null) {
            throw new IllegalArgumentException("Disciplina é obrigatória.");
        }

        for (OfertaDisciplina oferta : ofertas) {
            if (oferta.getDisciplina().getCodigo()
                    .equals(disciplina.getCodigo())) {

                throw new IllegalStateException(
                        "Disciplina já ofertada nesta turma."
                );
            }
        }

        OfertaDisciplina novaOferta =
                new OfertaDisciplina(disciplina, this);

        ofertas.add(novaOferta);

        return novaOferta;
    }

    public List<OfertaDisciplina> getOfertas() {
        return new ArrayList<>(ofertas);
    }

    @Override
    public String toString() {
        return codigo + " - " + periodoLetivo;
    }
}