package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;

public class Turma {

    private final String codigo;
    private final PeriodoLetivo periodoLetivo;
    private final List<OfertaDisciplina> ofertas;

    public Turma(String codigo, PeriodoLetivo periodoLetivo) {
        validarCodigo(codigo);
        validarPeriodoLetivo(periodoLetivo);

        this.codigo = codigo;
        this.periodoLetivo = periodoLetivo;
        this.ofertas = new ArrayList<>();
    }

    private void validarCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Código da turma não pode ser nulo ou vazio");
        }
    }

    private void validarPeriodoLetivo(PeriodoLetivo periodoLetivo) {
        if (periodoLetivo == null) {
            throw new IllegalArgumentException(
                    "Período letivo não pode ser nulo");
        }
    }

    public String getCodigo() {
        return codigo;
    }

    public PeriodoLetivo getPeriodoLetivo() {
        return periodoLetivo;
    }

    public List<OfertaDisciplina> getOfertas() {
        return List.copyOf(ofertas);
    }

    public OfertaDisciplina ofertarDisciplina(Disciplina disciplina) {

        for (OfertaDisciplina oferta : ofertas) {
            if (oferta.getDisciplina().getCodigo()
                    .equals(disciplina.getCodigo())) {

                throw new IllegalStateException(
                        "Disciplina já ofertada nesta turma: "
                                + disciplina.getCodigo());
            }
        }

        OfertaDisciplina oferta = new OfertaDisciplina(
                disciplina,
                this
        );

        ofertas.add(oferta);

        return oferta;
    }

    @Override
    public String toString() {
        return String.format(
                "Turma{codigo='%s', periodoLetivo=%s, ofertas=%s}",
                codigo,
                periodoLetivo,
                ofertas
        );
    }
}