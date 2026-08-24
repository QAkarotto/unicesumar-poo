package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;


public class OfertaDisciplina {

    private final Disciplina disciplina;
    private final Turma turma;
    private final List<Matricula> matriculas = new ArrayList<>();

    OfertaDisciplina(Disciplina disciplina, Turma turma) {
        if (disciplina == null) {
            throw new IllegalArgumentException("Disciplina da oferta é obrigatória.");
        }
        if (turma == null) {
            throw new IllegalArgumentException("Turma da oferta é obrigatória.");
        }

        this.disciplina = disciplina;
        this.turma = turma;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public Turma getTurma() {
        return turma;
    }

    
    public Matricula matricular(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno a ser matriculado é obrigatório.");
        }
        if (possuiMatriculaDe(aluno)) {
            throw new IllegalStateException(
                    "O aluno " + aluno.getIdentificadorAcademico() + " já possui matrícula nesta oferta.");
        }
        if (aluno.jaFoiAprovadoEm(disciplina)) {
            throw new IllegalStateException(
                    "O aluno " + aluno.getIdentificadorAcademico()
                            + " já foi aprovado em " + disciplina.getCodigo()
                            + " e não pode se matricular novamente.");
        }

        var matricula = new Matricula(aluno, this);
        matriculas.add(matricula);
        aluno.registrarMatricula(matricula);
        return matricula;
    }

   
    public List<Matricula> getMatriculas() {
        return List.copyOf(matriculas);
    }

    private boolean possuiMatriculaDe(Aluno aluno) {
        return matriculas.stream()
                .anyMatch(matricula -> matricula.getAluno() == aluno);
    }

    @Override
    public String toString() {
        return disciplina.getCodigo() + " (" + turma.getCodigo() + ")";
    }
}
