package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;

public class Turma {

    private final String codigo;
    private final PeriodoLetivo periodoLetivo;

    private final List<OfertaDisciplina> ofertas = new ArrayList<>();

    public Turma(
            String codigo,
            PeriodoLetivo periodoLetivo) {

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

    public OfertaDisciplina ofertarDisciplina(Disciplina disciplina) {

        if (disciplina == null) {
            throw new IllegalArgumentException(
                    "Disciplina é obrigatória."
            );
        }

        for (OfertaDisciplina oferta : ofertas) {

            if (oferta.getDisciplina().equals(disciplina)) {
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

    @Override
    public String toString() {

        String resultado =
                codigo + " - " +
                        periodoLetivo;

        for (OfertaDisciplina oferta : ofertas) {

            resultado += " - " +
                    oferta.getDisciplina().getNome();
        }

        return resultado;
    }
}