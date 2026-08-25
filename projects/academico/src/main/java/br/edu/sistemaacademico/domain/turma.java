package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Turma {

    private final String codigo;
    private final PeriodoLetivo periodoLetivo;
    private final List<OfertaDisciplina> ofertasDisciplinas;

    public Turma(String codigo, PeriodoLetivo periodoLetivo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("O código da turma é obrigatório.");
        }

        if (periodoLetivo == null) {
            throw new IllegalArgumentException("O período letivo é obrigatório.");
        }

        this.codigo = codigo.trim();
        this.periodoLetivo = periodoLetivo;
        this.ofertasDisciplinas = new ArrayList<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public PeriodoLetivo getPeriodoLetivo() {
        return periodoLetivo;
    }

    public List<OfertaDisciplina> getOfertasDisciplinas() {
        return Collections.unmodifiableList(ofertasDisciplinas);
    }

    public OfertaDisciplina ofertarDisciplina(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException("A disciplina é obrigatória.");
        }

        boolean disciplinaJaOfertada = ofertasDisciplinas.stream()
                .anyMatch(oferta ->
                        oferta.getDisciplina().equals(disciplina)
                );

        if (disciplinaJaOfertada) {
            throw new IllegalArgumentException(
                    "A disciplina já foi ofertada nesta turma."
            );
        }

        OfertaDisciplina oferta = new OfertaDisciplina(this, disciplina);
        ofertasDisciplinas.add(oferta);

        return oferta;
    }

    @Override
    public String toString() {
        return "Turma{" +
                "codigo='" + codigo + '\'' +
                ", periodoLetivo=" + periodoLetivo +
                ", ofertasDisciplinas=" + ofertasDisciplinas +
                '}';
    }
}
