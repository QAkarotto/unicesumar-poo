package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;

public class Turma {
    private String codigo;
    private PeriodoLetivo periodoLetivo;
    private List<OfertaDisciplina> ofertas = new ArrayList<>();

    public Turma(String codigo, PeriodoLetivo periodoLetivo) {
        this.codigo = codigo;
        this.periodoLetivo = periodoLetivo;
    }

    public String getCodigo() {
        return codigo;
    }

    public List<OfertaDisciplina> getOfertas() {
        return ofertas;
    }

    // Método principal: Ofertar a disciplina e criar a OfertaDisciplina
    public OfertaDisciplina ofertarDisciplina(Disciplina disciplina) {
        // Validação: Impede a mesma disciplina na mesma turma
        for (OfertaDisciplina oferta : ofertas) {
            if (oferta.getDisciplina().equals(disciplina)) {
                throw new IllegalArgumentException("A disciplina " + disciplina.getNome() + " já está ofertada nesta turma.");
            }
        }

        OfertaDisciplina novaOferta = new OfertaDisciplina(this, disciplina);
        this.ofertas.add(novaOferta);
        return novaOferta;
    }
}