package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;


public class OfertaDisciplina {

    private final Disciplina disciplina;
    private final Turma turma;
    private final List<Matricula> matriculas;

    public OfertaDisciplina(Disciplina disciplina, Turma turma) {
        validarDisciplina(disciplina);
        validarTurma(turma);

        this.disciplina = disciplina;
        this.turma = turma;
        this.matriculas = new ArrayList<>();
    }

    private void validarDisciplina(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException(
                    "Disciplina não pode ser nula"
            );
        }
    }

    private void validarTurma(Turma turma) {
        if (turma == null) {
            throw new IllegalArgumentException(
                    "Turma não pode ser nula"
            );
        }
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

        validarAluno(aluno);

        if (aluno.jaFoiAprovadoEm(disciplina)) {
            throw new IllegalStateException(
                    "Aluno já foi aprovado na disciplina: "
                            + disciplina.getCodigo()
            );
        }

        for (Matricula matricula : matriculas) {
            if (matricula.getAluno() == aluno) {
                throw new IllegalStateException(
                        "Aluno já possui matrícula nesta oferta"
                );
            }
        }

        String codigoMatricula =
                turma.getCodigo()
                        + "-"
                        + disciplina.getCodigo()
                        + "-"
                        + (matriculas.size() + 1);

        Matricula matricula = new Matricula(
                codigoMatricula,
                aluno,
                this
        );

        matriculas.add(matricula);
        aluno.adicionarMatricula(matricula);

        return matricula;
    }

    private void validarAluno(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException(
                    "Aluno não pode ser nulo"
            );
        }
    }

    @Override
    public String toString() {
        return String.format(
                "OfertaDisciplina{disciplina='%s', turma='%s'}",
                disciplina.getNome(),
                turma.getCodigo()
        );
    }
}