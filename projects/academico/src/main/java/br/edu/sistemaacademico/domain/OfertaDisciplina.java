package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class OfertaDisciplina {
    private final Turma turma;
    private final Disciplina disciplina;
    private final List<Matricula> matriculas;

    public OfertaDisciplina(Turma turma, Disciplina disciplina) {
        if (turma == null || disciplina == null) {
            throw new IllegalArgumentException("Turma e Disciplina são obrigatórias para a oferta.");
        }
        this.turma = turma;
        this.disciplina = disciplina;
        this.matriculas = new ArrayList<>();
    }

    public Matricula matricular(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno inválido para matrícula.");
        }

        // Validação de duplicidade na mesma oferta
        boolean jaMatriculado = matriculas.stream()
                .anyMatch(m -> m.getAluno().equals(aluno));
        if (jaMatriculado) {
            throw new IllegalStateException("Aluno não pode possuir duas matrículas na mesma oferta.");
        }

        // Validação de aprovação prévia na mesma disciplina
        if (aluno.jaFoiAprovadoEm(this.disciplina)) {
            throw new IllegalStateException("Aluno aprovado não pode cursar novamente a mesma disciplina.");
        }

        Matricula matricula = new Matricula(aluno, this);
        this.matriculas.add(matricula);
        aluno.registrarMatricula(matricula);
        return matricula;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OfertaDisciplina that)) return false;
        return Objects.equals(turma, that.turma) && Objects.equals(disciplina, that.disciplina);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turma, disciplina);
    }

    @Override
    public String toString() {
        return disciplina.getNome() + " (" + turma.getCodigo() + ")";
    }
}