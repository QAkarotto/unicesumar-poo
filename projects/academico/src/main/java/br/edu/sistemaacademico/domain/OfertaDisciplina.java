package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OfertaDisciplina {

    private final Turma turma;
    private final Disciplina disciplina;
    private final List<Matricula> matriculas;

    public OfertaDisciplina(
            Turma turma,
            Disciplina disciplina
    ) {
        if (turma == null) {
            throw new IllegalArgumentException(
                    "Turma é obrigatória."
            );
        }

        if (disciplina == null) {
            throw new IllegalArgumentException(
                    "Disciplina é obrigatória."
            );
        }

        this.turma = turma;
        this.disciplina = disciplina;
        this.matriculas = new ArrayList<>();
    }

    public Turma getTurma() {
        return turma;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public List<Matricula> getMatriculas() {
        return Collections.unmodifiableList(matriculas);
    }

    public Matricula matricular(Aluno aluno) {

        if (aluno == null) {
            throw new IllegalArgumentException(
                    "Aluno é obrigatório."
            );
        }

        if (possuiMatricula(aluno)) {
            throw new IllegalStateException(
                    "Aluno já possui matrícula nesta oferta."
            );
        }

        if (aluno.foiAprovadoNaDisciplina(disciplina)) {
            throw new IllegalStateException(
                    "Aluno já foi aprovado nesta disciplina."
            );
        }

        Matricula matricula =
                new Matricula(aluno, this);

        matriculas.add(matricula);
        aluno.adicionarMatricula(matricula);

        return matricula;
    }

    private boolean possuiMatricula(Aluno aluno) {
        return matriculas.stream()
                .anyMatch(matricula ->
                        matricula.getAluno().equals(aluno)
                );
    }

    @Override
    public String toString() {
        return turma.getCodigo()
                + " - "
                + disciplina.getCodigo();
    }
}