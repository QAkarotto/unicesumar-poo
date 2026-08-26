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

    public List<OfertaDisciplina> getOfertas() {
        return Collections.unmodifiableList(ofertas);
    }

    public OfertaDisciplina ofertarDisciplina(Disciplina disciplina) {
        boolean jaOfertada = ofertas.stream()
                .anyMatch(o -> o.getDisciplina().equals(disciplina));
        if (jaOfertada) {
            throw new IllegalArgumentException(
                    disciplina.getNome() + " já está ofertada nesta turma."
            );
        }

        OfertaDisciplina oferta = new OfertaDisciplina(disciplina, this);
        ofertas.add(oferta);
        return oferta;
    }

    @Override
    public String toString() {
        return codigo + " (" + periodoLetivo + ")";
    }
}