package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OfertaDisciplina {

    private final Turma turma;
    private final Disciplina disciplina;
    private final List<Matricula> matriculas;

    public OfertaDisciplina(Turma turma, Disciplina disciplina) {
        if (turma == null) {
            throw new IllegalArgumentException(
                    "A turma é obrigatória."
            );
        }

        if (disciplina == null) {
            throw new IllegalArgumentException(
                    "A disciplina é obrigatória."
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
                    "O aluno é obrigatório."
            );
        }

        boolean matriculaDuplicada = matriculas.stream()
                .anyMatch(matricula ->
                        matricula.getAluno().equals(aluno)
                );

        if (matriculaDuplicada) {
            throw new IllegalStateException(
                    "O aluno já está matriculado nesta oferta."
            );
        }

        if (aluno.jaFoiAprovadoEm(disciplina)) {
            throw new IllegalStateException(
                    "O aluno já foi aprovado nesta disciplina."
            );
        }

        Matricula matricula = new Matricula(aluno, this);

        matriculas.add(matricula);
        aluno.adicionarMatriculaAoHistorico(matricula);

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
