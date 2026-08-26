package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OfertaDisciplina {

    private Turma turma;
    private Disciplina disciplina;
    private List<Matricula> matriculas = new ArrayList<>();

    public OfertaDisciplina(Turma turma, Disciplina disciplina) {
        if (turma == null) {
            throw new IllegalArgumentException("Turma é obrigatória");
        }

        if (disciplina == null) {
            throw new IllegalArgumentException("Disciplina é obrigatória");
        }

        this.turma = turma;
        this.disciplina = disciplina;
    private final Disciplina disciplina;
    private final Turma turma;
    private final List<Matricula> matriculas = new ArrayList<>();

    OfertaDisciplina(Disciplina disciplina, Turma turma) {
        this.disciplina = disciplina;
        this.turma = turma;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public Turma getTurma() {
        return turma;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public Matricula matricular(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno é obrigatório");
        }

        for (Matricula matricula : matriculas) {
            if (matricula.getAluno().equals(aluno)) {
                throw new IllegalArgumentException(
                        "Aluno já está matriculado nesta oferta"
                );
            }
        }

        if (aluno.jaFoiAprovadoNaDisciplina(disciplina)) {
            throw new IllegalStateException(
                    "Aluno já foi aprovado nesta disciplina"
    public List<Matricula> getMatriculas() {
        return Collections.unmodifiableList(matriculas);
    }

    public Matricula matricular(Aluno aluno) {
        if (aluno.jaFoiAprovadoEm(disciplina)) {
            throw new IllegalStateException(
                    "Aluno já foi aprovado em " + disciplina.getNome() + " e não pode se matricular novamente."
            );
        }

        boolean jaMatriculado = matriculas.stream()
                .anyMatch(m -> m.getAluno().equals(aluno));
        if (jaMatriculado) {
            throw new IllegalArgumentException(
                    "Aluno já possui matrícula nesta oferta de " + disciplina.getNome() + "."
            );
        }

        Matricula matricula = new Matricula(aluno, this);

        matriculas.add(matricula);
        aluno.adicionarMatricula(matricula);

        return matricula;
    }

    public List<Matricula> getMatriculas() {
        return new ArrayList<>(matriculas);
    }

    @Override
    public String toString() {
        return disciplina + " - " + turma.getCodigo();
    }
}
        matriculas.add(matricula);
        aluno.registrarMatricula(matricula);
        return matricula;
    }

    @Override
    public String toString() {
        return disciplina.getNome() + " - " + turma.getCodigo();
    }
}
