package br.edu.sistemaacademico.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Turma {
    @Id
    private String codigo;

    @Embedded
    private PeriodoLetivo periodoLetivo;

    @OneToMany(mappedBy = "turma")
    private List<OfertaDisciplina> ofertas = new ArrayList<>();

    protected Turma() {
    }

    public Turma(String codigo, PeriodoLetivo periodoLetivo) {
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
        return ofertas;
    }

    public OfertaDisciplina ofertarDisciplina(Disciplina disciplina) {
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
