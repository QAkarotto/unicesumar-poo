package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OfertaDisciplina {
    private final Turma turma;
    private final Disciplina disciplina;
    private final List<Matricula> matriculas = new ArrayList<>();

    OfertaDisciplina(Turma turma, Disciplina disciplina) {
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

    public Matricula matricular(String codigoMatricula, Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno não pode ser nulo");
        }
        if (possuiMatricula(aluno)) {
            throw new IllegalStateException("Aluno já matriculado nesta oferta de disciplina");
        }
        if (aluno.jaAprovadoEm(disciplina)) {
            throw new IllegalStateException(
                    "Aluno já foi aprovado nesta disciplina e não pode se matricular novamente");
        }

        var matricula = new Matricula(codigoMatricula, aluno, this);
        matriculas.add(matricula);
        aluno.registrarMatricula(matricula);
        return matricula;
    }

    private boolean possuiMatricula(Aluno aluno) {
        return matriculas.stream().anyMatch(matricula -> matricula.getAluno().equals(aluno));
    }

    @Override
    public String toString() {
        return String.format("OfertaDisciplina(turma=%s, disciplina=%s)", turma.getCodigo(), disciplina.getNome());
    }
}
