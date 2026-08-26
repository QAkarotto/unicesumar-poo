package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Turma {
    private final String codigo;
    private final PeriodoLetivo periodoLetivo;
    private final List<OfertaDisciplina> ofertas;

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

    public OfertaDisciplina ofertarDisciplina(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException("Disciplina não pode ser nula.");
        }
        boolean jaOfertada = ofertas.stream()
                .anyMatch(o -> o.getDisciplina().equals(disciplina));

        if (jaOfertada) {
            throw new IllegalStateException("A mesma disciplina não pode ser ofertada duas vezes na mesma turma: " + disciplina.getNome());
        }

        OfertaDisciplina novaOferta = new OfertaDisciplina(this, disciplina);
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
        return Collections.unmodifiableList(ofertas);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Turma turma)) return false;
        return Objects.equals(codigo, turma.codigo) && Objects.equals(periodoLetivo, turma.periodoLetivo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo, periodoLetivo);
    }

    @Override
    public String toString() {
        return codigo + " — " + periodoLetivo;
    }
}