package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;

public class OfertaDisciplina {

    private Disciplina disciplina;
    private Turma turma;
    private List<Matricula> matriculas;

    public OfertaDisciplina(Disciplina disciplina, Turma turma) {

        if (disciplina == null) {
            throw new IllegalArgumentException("Disciplina é obrigatória.");
        }

        if (turma == null) {
            throw new IllegalArgumentException("Turma é obrigatória.");
        }

        this.disciplina = disciplina;
        this.turma = turma;
        this.matriculas = new ArrayList<>();
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public List<Matricula> getMatriculas() {
        return new ArrayList<>(matriculas);
    }

    public Matricula matricular(Aluno aluno) {

        if (aluno == null) {
            throw new IllegalArgumentException("Aluno é obrigatório.");
        }

        for (Matricula matricula : matriculas) {
            if (matricula.getAluno() == aluno) {
                throw new IllegalStateException(
                        "Aluno já matriculado nesta oferta."
                );
            }
        }

        if (aluno.jaFoiAprovado(disciplina)) {
            throw new IllegalStateException(
                    "Aluno já foi aprovado nesta disciplina."
            );
        }

        Matricula novaMatricula =
                new Matricula(aluno, this);

        matriculas.add(novaMatricula);
        aluno.adicionarMatricula(novaMatricula);

        return novaMatricula;
    }

    @Override
    public String toString() {
        return disciplina + " - " + turma;
    }
}