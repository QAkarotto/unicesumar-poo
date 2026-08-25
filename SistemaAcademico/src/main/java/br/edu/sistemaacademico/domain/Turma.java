package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Turma {

    private final String codigo;
    private final PeriodoLetivo periodoLetivo;
    private final List<OfertaDisciplina> ofertas = new ArrayList<>();

    public Turma(String codigo, PeriodoLetivo periodoLetivo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("O código da turma é obrigatório.");
        }
        if (periodoLetivo == null) {
            throw new IllegalArgumentException("O período letivo é obrigatório.");
        }
        this.codigo = codigo.trim();
        this.periodoLetivo = periodoLetivo;
    }

    public String getCodigo() {
        return codigo;
    }

    public PeriodoLetivo getPeriodoLetivo() {
        return periodoLetivo;
    }

    public OfertaDisciplina ofertarDisciplina(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException("A disciplina é obrigatória.");
        }
        if (possuiOferta(disciplina)) {
            throw new IllegalStateException("A disciplina " + disciplina.getNome()
                    + " já está ofertada para a turma " + codigo + " em " + periodoLetivo + ".");
        }
        OfertaDisciplina oferta = new OfertaDisciplina(this, disciplina);
        ofertas.add(oferta);
        return oferta;
    }

    public boolean possuiOferta(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException("A disciplina é obrigatória.");
        }
        for (OfertaDisciplina oferta : ofertas) {
            if (oferta.refereSeA(disciplina)) {
                return true;
            }
        }
        return false;
    }

    public OfertaDisciplina buscarOferta(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException("A disciplina é obrigatória.");
        }
        for (OfertaDisciplina oferta : ofertas) {
            if (oferta.refereSeA(disciplina)) {
                return oferta;
            }
        }
        throw new IllegalArgumentException("A disciplina " + disciplina.getNome()
                + " não é ofertada pela turma " + codigo + ".");
    }

    public List<OfertaDisciplina> getOfertas() {
        return Collections.unmodifiableList(ofertas);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Turma)) {
            return false;
        }
        Turma outra = (Turma) obj;
        return codigo.equalsIgnoreCase(outra.codigo) && periodoLetivo.equals(outra.periodoLetivo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo.toLowerCase(), periodoLetivo);
    }

    @Override
    public String toString() {
        StringBuilder descricao = new StringBuilder(codigo + " - " + periodoLetivo);
        for (OfertaDisciplina oferta : ofertas) {
            descricao.append(" - ").append(oferta.getDisciplina().getNome());
        }
        return descricao.toString();
    }
}