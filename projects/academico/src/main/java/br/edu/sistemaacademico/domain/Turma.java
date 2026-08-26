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
        this.codigo = codigo;
        this.periodoLetivo = periodoLetivo;
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

    /**
     * Oferta uma disciplina para esta turma.
     * Impede inclusão duplicada da mesma disciplina.
     */
    public OfertaDisciplina oferecer(Disciplina disciplina) {
        boolean jaOfertada = ofertas.stream()
                .anyMatch(o -> o.getDisciplina().equals(disciplina));

        if (jaOfertada) {
            throw new IllegalArgumentException(
                "Disciplina " + disciplina.getNome() + " já está ofertada na turma " + codigo + "."
            );
        }

        OfertaDisciplina oferta = new OfertaDisciplina(this, disciplina);
        ofertas.add(oferta);
        return oferta;
    }

    /**
     * Busca a oferta de uma disciplina nesta turma.
     */
    public OfertaDisciplina buscarOferta(Disciplina disciplina) {
        return ofertas.stream()
                .filter(o -> o.getDisciplina().equals(disciplina))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                    "Disciplina " + disciplina.getNome() + " não está ofertada na turma " + codigo + "."
                ));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Turma)) return false;
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
