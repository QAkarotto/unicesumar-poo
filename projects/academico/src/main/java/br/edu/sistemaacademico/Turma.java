package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Turma {
    private final String codigo;
    private final PeriodoLetivo periodoLetivo;
    private final List<OfertaDisciplina> ofertas;

    public Turma(String codigo, PeriodoLetivo periodoLetivo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("O código da turma não pode ser nulo ou vazio.");
        }
        if (periodoLetivo == null) {
            throw new IllegalArgumentException("O período letivo não pode ser nulo.");
        }

        this.codigo = codigo;
        this.periodoLetivo = periodoLetivo;
        this.ofertas = new ArrayList<>();
    }

    public OfertaDisciplina ofertarDisciplina(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException("A disciplina não pode ser nula.");
        }

        boolean jaOfertada = ofertas.stream()
                .anyMatch(oferta -> oferta.getDisciplina().equals(disciplina));

        if (jaOfertada) {
            throw new IllegalStateException("A mesma disciplina não pode ser ofertada duas vezes na mesma turma.");
        }

        OfertaDisciplina novaOferta = new OfertaDisciplina(this, disciplina);
        ofertas.add(novaOferta);
        return novaOferta;
    }

    public String getCodigo() {
        return codigo;
    }

    public List<OfertaDisciplina> getOfertas() {
        return Collections.unmodifiableList(ofertas);
    }

    @Override
    public String toString() {
        return String.format("Turma[%s - %s]", codigo, periodoLetivo);
    } // João Pedro Hulchak Kazmierzak RA: 25141620-2 e Hiuri Luciano dos Santos RA: 25208360-2
}