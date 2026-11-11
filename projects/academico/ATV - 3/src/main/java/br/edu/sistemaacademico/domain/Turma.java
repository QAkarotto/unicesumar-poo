package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Turma {

    private final String codigo;
    private final PeriodoLetivo periodoLetivo;
    private final List<OfertaDisciplina> ofertas = new ArrayList<>();

    public Turma(String codigo, PeriodoLetivo periodoLetivo) {
        this.codigo = codigo;
        this.periodoLetivo = periodoLetivo;
    }

    public String getCodigo() {
        return codigo;
    }

    public PeriodoLetivo getPeriodoLetivo() {
        return periodoLetivo;
    }

    public OfertaDisciplina ofertarDisciplina(Disciplina disciplina) {
        for (OfertaDisciplina oferta : ofertas) {
            if (oferta.getDisciplina().getCodigo().equals(disciplina.getCodigo())) {
                throw new IllegalStateException(
                        "A disciplina já foi ofertada nesta turma."
                );
            }
        }

        OfertaDisciplina oferta = new OfertaDisciplina(this, disciplina);
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