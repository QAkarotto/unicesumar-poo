package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;

public class OfertaDisciplina {

    private final Turma turma;
    private final Disciplina disciplina;

    private final List<Matricula> matriculas =
            new ArrayList<>();

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
    }

    public Turma getTurma() {
        return turma;
    }

    public Disciplina getDisciplina() {
        return disciplina;
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

        boolean matriculaDuplicada = matriculas.stream()
                .anyMatch(matricula ->
                        matricula.getAluno().equals(aluno)
                );

        if (matriculaDuplicada) {
            throw new IllegalStateException(
                    "O aluno já está matriculado nesta oferta."
            );
        }

        if (aluno.jaFoiAprovado(disciplina)) {
            throw new IllegalStateException(
                    "O aluno já foi aprovado nesta disciplina."
            );
        }

        Matricula matricula =
                new Matricula(aluno, this);

        matriculas.add(matricula);
        aluno.registrarMatricula(matricula);

        return matricula;
    }

    @Override
    public String toString() {
        return disciplina + " - " + turma.getCodigo();
    }
}