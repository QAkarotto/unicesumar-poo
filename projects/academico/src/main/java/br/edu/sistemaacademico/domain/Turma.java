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
            throw new IllegalArgumentException("Código da turma não pode ser vazio");
        }
        if (periodoLetivo == null) {
            throw new IllegalArgumentException("Período letivo não pode ser nulo");
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
        return Collections.unmodifiableList(ofertas);
    }

    public OfertaDisciplina ofertarDisciplina(Disciplina disciplina) {
        boolean jaOfertada = ofertas.stream()
                .anyMatch(o -> o.getDisciplina().getCodigo().equals(disciplina.getCodigo()));

        if (jaOfertada) {
            throw new IllegalArgumentException("A mesma disciplina não pode ser ofertada duas vezes na mesma turma.");
        }

        OfertaDisciplina novaOferta = new OfertaDisciplina(this, disciplina);
        ofertas.add(novaOferta);
        return novaOferta;
    }

    @Override
    public String toString() {
        return codigo + " (" + periodoLetivo + ")";
    }
}