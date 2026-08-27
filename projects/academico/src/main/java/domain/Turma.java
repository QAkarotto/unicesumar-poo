package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Turma {

    private final String codigo;
    private final PeriodoLetivo periodoLetivo;
    private final List<OfertaDisciplina> ofertas = new ArrayList<>();

    public Turma(String codigo, PeriodoLetivo periodoLetivo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException(
                    "O código da turma não pode ser vazio."
            );
        }
        if (periodoLetivo == null) {
            throw new IllegalArgumentException(
                    "O período letivo não pode ser nulo."
            );
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

    public List<OfertaDisciplina> getOfertas() {
        return Collections.unmodifiableList(ofertas);
    }

    public OfertaDisciplina ofertarDisciplina(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException("A disciplina não pode ser nula.");
        }

        boolean jaOfertada = ofertas.stream()
                .anyMatch(oferta -> oferta.getDisciplina().equals(disciplina));
        if (jaOfertada) {
            throw new IllegalArgumentException(
                    "A disciplina " + disciplina.getNome() + " já está ofertada na turma " + codigo + "."
            );
        }

        OfertaDisciplina oferta = new OfertaDisciplina(this, disciplina);
        ofertas.add(oferta);
        return oferta;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(codigo).append(" — ").append(periodoLetivo);
        for (OfertaDisciplina oferta : ofertas) {
            sb.append(" - ").append(oferta.getDisciplina().getNome());
        }
        return sb.toString();
    }
}
