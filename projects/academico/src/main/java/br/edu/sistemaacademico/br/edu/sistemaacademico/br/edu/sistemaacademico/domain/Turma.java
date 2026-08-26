package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;

public class Turma {

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

        this.codigo = codigo;
        this.periodoLetivo = periodoLetivo;
    }

    public String getCodigo() {
        return codigo;
    }

    public PeriodoLetivo getPeriodoLetivo() {
        return periodoLetivo;
    }


    // A turma é quem conhece suas ofertas e quem garante que a mesma disciplina não seja ofertada duas vezes.


    public OfertaDisciplina ofertarDisciplina(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException("A disciplina é obrigatória.");
        }

        for (OfertaDisciplina oferta : ofertas) {
            if (oferta.getDisciplina().equals(disciplina)) {
                throw new IllegalStateException(
                        "A disciplina " + disciplina.getNome()
                                + " já está ofertada na turma " + getCodigo() + "."
                );
            }
        }

        var oferta = new OfertaDisciplina(disciplina, this);
        ofertas.add(oferta);

        return oferta;
    }

    public List<OfertaDisciplina> getOfertas() {
        List<OfertaDisciplina> copia = new ArrayList<>();

        for (OfertaDisciplina oferta : ofertas) {
            copia.add(oferta);
        }

        return copia;
    }

    @Override
    public String toString() {
        return "Código: " + getCodigo() +
                "\nPeríodo letivo: " + periodoLetivo.getAno() +
                " - " + periodoLetivo.getSemestre();
    }
}