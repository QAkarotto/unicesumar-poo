package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
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
            throw new IllegalArgumentException("Código da turma não pode ser vazio");
        }
    }

    private void validarPeriodoLetivo(PeriodoLetivo periodoLetivo) {
        if (periodoLetivo == null) {
            throw new IllegalArgumentException("Período letivo não pode ser nulo");
        }
    }

    public String getCodigo() {
        return codigo;
    }

    public PeriodoLetivo getPeriodoLetivo() {
        return periodoLetivo;
    }

    public void adicionarOferta(OfertaDisciplina oferta) {
        if (oferta == null) {
            throw new IllegalArgumentException("Oferta não pode ser nula");
        }

        if (possuiOfertaDuplicada(oferta.getDisciplina())) {
            throw new IllegalStateException(
                "Disciplina " + oferta.getDisciplina().getNome() +
                " já está ofertada nesta turma"
            );
        }

        ofertas.add(oferta);
    }

    private boolean possuiOfertaDuplicada(Disciplina disciplina) {
        return ofertas.stream()
                .anyMatch(o -> o.getDisciplina().getCodigo().equals(disciplina.getCodigo()));
    }

    public List<OfertaDisciplina> getOfertas() {
        return Collections.unmodifiableList(ofertas);
    }

    @Override
    public String toString() {
        return "Turma{" +
                "codigo='" + codigo + '\'' +
                ", periodoLetivo=" + periodoLetivo.getAno() +
                " " + periodoLetivo.getSemestre().getDescricao() +
                ", ofertas=" + ofertas.size() +
                '}';
    }
}
