package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Turma {

    private final String codigo;
    private final PeriodoLetivo periodoLetivo;

    private final List<OfertaDisciplina> ofertas =
            new ArrayList<>();

    public Turma(
            String codigo,
            PeriodoLetivo periodoLetivo) {

        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "O código da turma é obrigatório.");
        }

        if (periodoLetivo == null) {
            throw new IllegalArgumentException(
                    "O período letivo é obrigatório.");
        }

        this.codigo = codigo.trim();
        this.periodoLetivo = periodoLetivo;
    }

    // Compatibilidade com a versão inicial da atividade.
    public Turma(
            String codigo,
            Disciplina disciplina,
            PeriodoLetivo periodoLetivo) {

        this(codigo, periodoLetivo);
        adicionarDisciplina(disciplina);
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

    public OfertaDisciplina adicionarDisciplina(
            Disciplina disciplina) {

        if (disciplina == null) {
            throw new IllegalArgumentException(
                    "A disciplina não pode ser nula.");
        }

        for (OfertaDisciplina oferta : ofertas) {

            if (oferta.getDisciplina().equals(disciplina)) {
                throw new IllegalArgumentException(
                        "A disciplina já está ofertada nesta turma.");
            }
        }

        OfertaDisciplina oferta =
                new OfertaDisciplina(disciplina, this);

        ofertas.add(oferta);

        return oferta;
    }

    public OfertaDisciplina ofertarDisciplina(
            Disciplina disciplina) {

        return adicionarDisciplina(disciplina);
    }

    @Override
    public String toString() {

        StringBuilder resultado =
                new StringBuilder();

        resultado.append(codigo)
                .append(" — ")
                .append(periodoLetivo);

        if (!ofertas.isEmpty()) {

            resultado.append(" - ");

            for (int i = 0; i < ofertas.size(); i++) {

                if (i > 0) {
                    resultado.append(" - ");
                }

                resultado.append(
                        ofertas.get(i)
                                .getDisciplina()
                                .getNome());
            }
        }

        return resultado.toString();
    }
}
