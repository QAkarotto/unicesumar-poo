package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Turma {
    
    private final String codigo;
    private final String periodoLetivo;
    private final List<OfertaDisciplina> ofertas;

    public Turma(String codigo, String periodoLetivo) {

        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException(
                "Código da turma não pode ser nulo ou vazio."
            );
        }

        if (periodoLetivo == null || periodoLetivo.isBlank()) {
            throw new IllegalArgumentException(
                "Período letivo não pode ser nulo ou vazio."
            );
        }

        this.codigo = codigo.trim();
        this.periodoLetivo = periodoLetivo.trim();
        this.ofertas = new ArrayList<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public String getPeriodoLetivo() {
        return periodoLetivo;
    }
    public List<OfertaDisciplina> getOfertas() {
        return Collections.unmodifiableList(ofertas);
    }

     public OfertaDisciplina oferecer(Disciplina disciplina) {

        if (disciplina == null) {
            throw new IllegalArgumentException(
                "Disciplina não pode ser nula."
            );
        }

        boolean jaOfertada = ofertas.stream()
                .anyMatch(o ->
                    o.getDisciplina().equals(disciplina)
                );

        if (jaOfertada) {
            throw new IllegalArgumentException(
                "Disciplina "
                + disciplina.getNome()
                + " já está ofertada na turma "
                + codigo
                + "."
            );
        }

        OfertaDisciplina oferta =
                new OfertaDisciplina(this, disciplina);

        ofertas.add(oferta);

        return oferta;
    }
     public OfertaDisciplina buscarOferta(Disciplina disciplina) {

        if (disciplina == null) {
            throw new IllegalArgumentException(
                "Disciplina não pode ser nula."
            );
        }

        return ofertas.stream()
                .filter(o ->
                    o.getDisciplina().equals(disciplina)
                )
                .findFirst()
                .orElseThrow(() ->
                    new IllegalArgumentException(
                        "Disciplina "
                        + disciplina.getNome()
                        + " não está ofertada na turma "
                        + codigo
                        + "."
                    )
                );
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Turma)) {
            return false;
        }

        Turma that = (Turma) o;

        return Objects.equals(codigo, that.codigo)
                && Objects.equals(periodoLetivo, that.periodoLetivo);
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

