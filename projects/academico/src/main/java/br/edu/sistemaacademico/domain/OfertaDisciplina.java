package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    public Turma getTurma() {
        return turma;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public PeriodoLetivo getPeriodoLetivo() {
        return turma.getPeriodoLetivo();
    }

    public Matricula matricular(String codigo, Aluno aluno) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "O código da matrícula é obrigatório."
            );
        }

        if (aluno == null) {
            throw new IllegalArgumentException(
                    "O aluno é obrigatório."
            );
        }

        if (possuiMatriculaDe(aluno)) {
            throw new IllegalArgumentException(
                    "O aluno " + aluno.getNome()
                            + " já possui matrícula em " + this + "."
            );
        }

        if (possuiMatriculaComCodigo(codigo)) {
            throw new IllegalArgumentException(
                    "Já existe uma matrícula com o código "
                            + codigo.trim() + "."
            );
        }

        if (aluno.foiAprovadoEm(disciplina)) {
            throw new IllegalStateException(
                    "O aluno " + aluno.getNome()
                            + " já foi aprovado em "
                            + disciplina.getNome()
                            + " e não pode cursar a disciplina novamente."
            );
        }

        Matricula matricula = new Matricula(
                codigo,
                aluno,
                this
        );

        matriculas.add(matricula);
        aluno.registrarMatricula(matricula);

        return matricula;
    }

    public Matricula matricular(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException(
                    "O aluno é obrigatório."
            );
        }

        throw new IllegalArgumentException(
                "O código da matrícula é obrigatório."
        );
    }

    public boolean possuiMatriculaDe(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException(
                    "O aluno é obrigatório."
            );
        }

        for (Matricula matricula : matriculas) {
            if (aluno.equals(matricula.getAluno())) {
                return true;
            }
        }

        return false;
    }

    public boolean possuiMatriculaComCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "O código da matrícula é obrigatório."
            );
        }

        for (Matricula matricula : matriculas) {
            if (codigo.trim().equalsIgnoreCase(matricula.getCodigo())) {
                return true;
            }
        }

        return false;
    }

    public Matricula buscarMatricula(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException(
                    "O aluno é obrigatório."
            );
        }

        for (Matricula matricula : matriculas) {
            if (aluno.equals(matricula.getAluno())) {
                return matricula;
            }
        }

        throw new IllegalArgumentException(
                "O aluno " + aluno.getNome()
                        + " não possui matrícula em " + this + "."
        );
    }

    public List<Matricula> getMatriculas() {
        return Collections.unmodifiableList(matriculas);
    }

    public int getTotalMatriculados() {
        return matriculas.size();
    }

    boolean refereSeA(Disciplina outraDisciplina) {
        return disciplina.equals(outraDisciplina);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof OfertaDisciplina)) {
            return false;
        }

        OfertaDisciplina outra = (OfertaDisciplina) obj;

        return turma.equals(outra.turma)
                && disciplina.equals(outra.disciplina);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turma, disciplina);
    }

    @Override
    public String toString() {
        return disciplina.getNome()
                + " ("
                + turma.getCodigo()
                + " - "
                + turma.getPeriodoLetivo()
                + ")";
    }
}

