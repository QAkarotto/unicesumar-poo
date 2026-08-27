package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OfertaDisciplina {

    private final Turma turma;
    private final Disciplina disciplina;
    private final List<Matricula> matriculas = new ArrayList<>();

    public OfertaDisciplina(Turma turma, Disciplina disciplina) {
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
        for (Matricula matricula : matriculas) {
            if (matricula.getAluno().getIdentificadorAcademico()
        .equals(aluno.getIdentificadorAcademico())) {
                throw new IllegalStateException(
                        "O aluno já está matriculado nesta oferta."
                );
            }
        }

        Matricula matricula = new Matricula(aluno, this);
        matriculas.add(matricula);
        aluno.adicionarMatricula(matricula);

        return matricula;
    }

    @Override
    public String toString() {
        return "OfertaDisciplina{" +
                "turma=" + turma.getCodigo() +
                ", disciplina=" + disciplina +
                '}';
    }
}