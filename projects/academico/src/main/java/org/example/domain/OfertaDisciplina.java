package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;

public class OfertaDisciplina {

    private final Disciplina disciplina;
    private final Turma turma;

    private final List<Matricula> matriculas = new ArrayList<>();

    public OfertaDisciplina(
            Disciplina disciplina,
            Turma turma) {

        if (disciplina == null) {
            throw new IllegalArgumentException(
                    "Disciplina é obrigatória."
            );
        }

        if (turma == null) {
            throw new IllegalArgumentException(
                    "Turma é obrigatória."
            );
        }

        this.disciplina = disciplina;
        this.turma = turma;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public Turma getTurma() {
        return turma;
    }

    public List<Matricula> getMatriculas() {
        return List.copyOf(matriculas);
    }

    public Matricula matricular(Aluno aluno) {

        if (aluno == null) {
            throw new IllegalArgumentException(
                    "Aluno é obrigatório."
            );
        }

        if (aluno.possuiAprovacaoEm(disciplina)) {
            throw new IllegalStateException(
                    "Aluno já foi aprovado nesta disciplina."
            );
        }

        for (Matricula matricula : matriculas) {

            if (matricula.getAluno().equals(aluno)) {
                throw new IllegalStateException(
                        "Aluno já está matriculado nesta oferta."
                );
            }
        }

        Matricula matricula =
                new Matricula(this, aluno);

        matriculas.add(matricula);

        aluno.adicionarMatricula(matricula);

        return matricula;
    }

    @Override
    public String toString() {

        return "Oferta: " +
                disciplina.getNome() +
                " - Turma: " +
                turma.getCodigo();
    }
}