package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OfertaDisciplina {

    private final Disciplina disciplina;
    private final Turma turma;

    private final List<Matricula> matriculas;

    public OfertaDisciplina(
            Disciplina disciplina,
            Turma turma
    ) {
        if (disciplina == null) {
            throw new IllegalArgumentException(
                    "A disciplina não pode ser nula."
            );
        }

        if (turma == null) {
            throw new IllegalArgumentException(
                    "A turma não pode ser nula."
            );
        }

        this.disciplina = disciplina;
        this.turma = turma;
        this.matriculas = new ArrayList<>();
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public Turma getTurma() {
        return turma;
    }

    public List<Matricula> getMatriculas() {
        return Collections.unmodifiableList(matriculas);
    }

    public Matricula matricular(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException(
                    "O aluno não pode ser nulo."
            );
        }

        if (aluno.possuiAprovacaoEm(disciplina)) {
            throw new IllegalStateException(
                    "O aluno já foi aprovado nesta disciplina."
            );
        }

        if (possuiMatricula(aluno)) {
            throw new IllegalStateException(
                    "O aluno já possui matrícula nesta oferta."
            );
        }

        String codigoMatricula = gerarCodigoMatricula(aluno);

        Matricula matricula = new Matricula(
                codigoMatricula,
                aluno,
                this
        );

        matriculas.add(matricula);
        aluno.adicionarMatricula(matricula);

        return matricula;
    }

    private boolean possuiMatricula(Aluno aluno) {
        for (Matricula matricula : matriculas) {
            if (matricula.getAluno() == aluno) {
                return true;
            }
        }

        return false;
    }

    private String gerarCodigoMatricula(Aluno aluno) {
        return "MAT-" +
                aluno.getRa() +
                "-" +
                disciplina.getCodigo() +
                "-" +
                turma.getCodigo();
    }

    @Override
    public String toString() {
        return "OfertaDisciplina{" +
                "disciplina=" + disciplina +
                ", turma=" + turma.getCodigo() +
                '}';
    }
}
