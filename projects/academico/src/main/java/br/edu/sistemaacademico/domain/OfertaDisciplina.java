package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;



    public class OfertaDisciplina {
    private final Turma turma;
    private final Disciplina disciplina;
    private final List<Matricula> matriculas;

     OfertaDisciplina(Turma turma, Disciplina disciplina) {

        if (turma == null) {
            throw new IllegalArgumentException(
                "Turma não pode ser nula."
            );
        }

        if (disciplina == null) {
            throw new IllegalArgumentException(
                "Disciplina não pode ser nula."
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
                "Aluno não pode ser nulo."
            );
        }

     boolean jaMatriculadoAqui = matriculas.stream()
                .anyMatch(m ->
                    m.getAluno().equals(aluno)
                    && !m.isConcluida()
                );

        if (jaMatriculadoAqui) {
            throw new IllegalStateException(
                "Aluno "
                + aluno.getNome()
                + " já está matriculado em "
                + disciplina.getNome()
                + " nesta oferta."
            );
        }
         if (aluno.foiAprovadoEm(disciplina)) {
            throw new IllegalStateException(
                "Aluno "
                + aluno.getNome()
                + " já foi aprovado em "
                + disciplina.getNome()
                + " e não pode se matricular novamente."
            );
        }

        Matricula matricula =
                new Matricula(aluno, this);

        matriculas.add(matricula);
        aluno.adicionarMatricula(matricula);

        return matricula;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof OfertaDisciplina)) {
            return false;
        }

        OfertaDisciplina that = (OfertaDisciplina) o;

        return Objects.equals(turma, that.turma)
                && Objects.equals(disciplina, that.disciplina);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turma, disciplina);
    }

    @Override
    public String toString() {
        return disciplina.getNome()
                + " @ "
                + turma.getCodigo();
    }
}

