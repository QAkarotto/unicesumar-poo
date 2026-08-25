package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;

public class OfertaDisciplina {
    private final Turma turma;
    private final Disciplina disciplina;
    private final List<Matricula> matriculas = new ArrayList<>();

    OfertaDisciplina(Turma turma, Disciplina disciplina) {
        if (turma == null) {
            throw new IllegalArgumentException("A turma é obrigatória.");
        }
        if (disciplina == null) {
            throw new IllegalArgumentException("A disciplina é obrigatória.");
        }
        this.turma = turma;
        this.disciplina = disciplina;
    }

    public Matricula matricular(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("O aluno é obrigatório.");
        }
        boolean jaMatriculado = matriculas.stream()
                .anyMatch(matricula -> matricula.getAluno().equals(aluno));
        if (jaMatriculado) {
            throw new IllegalStateException("O aluno já possui matrícula nesta oferta.");
        }
        if (aluno.foiAprovadoEm(disciplina)) {
            throw new IllegalStateException("O aluno já foi aprovado nesta disciplina.");
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
    public String toString() {
        return disciplina + " (" + turma.getCodigo() + " - " + turma.getPeriodoLetivo() + ")";
    }
}
