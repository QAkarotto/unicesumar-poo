package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;


public class Turma {

    private final String codigo;
    private final PeriodoLetivo periodoLetivo;
    private final List<OfertaDisciplina> ofertas = new ArrayList<>();

    public Turma(String codigo, PeriodoLetivo periodoLetivo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código da turma é obrigatório.");
        }
        if (periodoLetivo == null) {
            throw new IllegalArgumentException("Período letivo da turma é obrigatório.");
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

    public OfertaDisciplina ofertarDisciplina(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException("Disciplina a ser ofertada é obrigatória.");
        }
        if (possuiOfertaDe(disciplina)) {
            throw new IllegalStateException(
                    "A disciplina " + disciplina.getCodigo() + " já está ofertada na turma " + codigo + ".");
        }

        var oferta = new OfertaDisciplina(disciplina, this);
        ofertas.add(oferta);
        return oferta;
    }

   
    public List<OfertaDisciplina> getOfertas() {
        return List.copyOf(ofertas);
    }

    private boolean possuiOfertaDe(Disciplina disciplina) {
        return ofertas.stream()
                .anyMatch(oferta -> oferta.getDisciplina().equals(disciplina));
    }

    @Override
    public String toString() {
        return "Turma{" +
                "codigo='" + codigo + '\'' +
                ", periodoLetivo=" + periodoLetivo +
                ", ofertas=" + ofertas.size() +
                '}';
    }
}
