package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;

public final class OfertaDisciplina {
    private final Turma turma;
    private final Disciplina disciplina;
    private final List<Matricula> matriculas = new ArrayList<>();

    OfertaDisciplina(Turma turma, Disciplina disciplina) {
        this.turma = turma;
        this.disciplina = disciplina;
    }

    public Matricula matricular(Aluno aluno) {
        var codigo = "MAT-" + (matriculas.size() + 1);
        return matricular(codigo, aluno);
    }

    public Matricula matricular(String codigo, Aluno aluno) {
        return new Matricula(codigo, aluno, this);
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

    void validarNovaMatricula(Aluno aluno) {
        boolean alunoJaMatriculado = matriculas.stream()
                .anyMatch(matricula -> matricula.getAluno().equals(aluno));

        if (alunoJaMatriculado) {
            throw new IllegalArgumentException(
                    "O aluno já está matriculado nesta oferta."
            );
        }
    }

    void registrarMatricula(Matricula matricula) {
        matriculas.add(matricula);
    }

    @Override
    public String toString() {
        return disciplina.getCodigo() + " - " + turma;
    }
}