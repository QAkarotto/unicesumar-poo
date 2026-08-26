package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OfertaDisciplina {
    private final Turma turma;
    private final Disciplina disciplina;
    private final List<Matricula> matriculas = new ArrayList<>();

    OfertaDisciplina(Turma turma, Disciplina disciplina) {
        if (turma == null || disciplina == null) {
            throw new IllegalArgumentException("Turma e disciplina são obrigatórias.");
        }
        this.turma = turma;
        this.disciplina = disciplina;
    }

    public Matricula matricular(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("O aluno é obrigatório.");
        }

        if (aluno.foiAprovadoEm(this.disciplina)) {
            throw new IllegalStateException("Aluno já foi aprovado nesta disciplina em outro período.");
        }

        boolean jaMatriculado = matriculas.stream()
                .anyMatch(m -> m.getAluno().equals(aluno));
        if (jaMatriculado) {
            throw new IllegalArgumentException("Aluno já está matriculado nesta oferta de disciplina.");
        }

        Matricula matricula = new Matricula(aluno, this);
        matriculas.add(matricula);
        aluno.registrarMatricula(matricula);

        return matricula;
    }

    public Turma getTurma() { return turma; }
    public Disciplina getDisciplina() { return disciplina; }
    public List<Matricula> getMatriculas() { return List.copyOf(matriculas); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof OfertaDisciplina outra)) return false;
        return turma.equals(outra.turma) && disciplina.equals(outra.disciplina);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turma, disciplina);
    }

    @Override
    public String toString() {
        return turma.getCodigo() + " - " + disciplina.getNome();
    }
}