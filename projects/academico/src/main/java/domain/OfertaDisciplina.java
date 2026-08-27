package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OfertaDisciplina {

    private final Turma turma;
    private final Disciplina disciplina;
    private final List<Matricula> matriculas = new ArrayList<>();

    OfertaDisciplina(Turma turma, Disciplina disciplina) {
        if (turma == null) {
            throw new IllegalArgumentException("A turma não pode ser nula.");
        }
        if (disciplina == null) {
            throw new IllegalArgumentException("A disciplina não pode ser nula.");
        }
        this.turma = turma;
        this.disciplina = disciplina;
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
            throw new IllegalArgumentException("O aluno não pode ser nulo.");
        }

        if (aluno.jaAprovadoEm(disciplina)) {
            throw new IllegalStateException(
                    "O aluno " + aluno.getNome() + " já foi aprovado em "
                            + disciplina.getNome() + " e não pode se matricular novamente."
            );
        }

        boolean jaMatriculado = matriculas.stream()
                .anyMatch(m -> m.getAluno().equals(aluno));
        if (jaMatriculado) {
            throw new IllegalStateException(
                    "O aluno " + aluno.getNome() + " já está matriculado nesta oferta de "
                            + disciplina.getNome() + "."
            );
        }

        Matricula matricula = new Matricula(this, aluno);
        matriculas.add(matricula);
        aluno.registrarMatricula(matricula);
        return matricula;
    }

    @Override
    public String toString() {
        return disciplina.getCodigo() + " - " + disciplina.getNome();
    }
}
