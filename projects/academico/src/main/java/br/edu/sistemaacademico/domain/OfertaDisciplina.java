package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class OfertaDisciplina {

    private final Turma turma;
    private final Disciplina disciplina;
    private final List<Matricula> matriculas;

    // Construtor package-private: só Turma cria OfertaDisciplina
    OfertaDisciplina(Turma turma, Disciplina disciplina) {
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

    /**
     * Realiza a matrícula de um aluno nesta oferta.
     * Regras validadas aqui:
     *   1. Matrícula duplicada na mesma oferta
     *   2. Aluno já aprovado na disciplina (em qualquer oferta/turma/período)
     */
    public Matricula matricular(Aluno aluno) {
        // Regra 1: matrícula duplicada na mesma oferta
        boolean jaMatriculadoAqui = matriculas.stream()
                .anyMatch(m -> m.getAluno().equals(aluno) && !m.isConcluida());

        if (jaMatriculadoAqui) {
            throw new IllegalStateException(
                    "Aluno " + aluno.getNome() + " já está matriculado em "
                            + disciplina.getNome() + " nesta oferta."
            );
        }

        // Regra 2: aluno aprovado em disciplina — não pode cursar novamente
        if (aluno.foiAprovadoEm(disciplina)) {
            throw new IllegalStateException(
                    "Aluno " + aluno.getNome() + " já foi aprovado em "
                            + disciplina.getNome() + " e não pode se matricular novamente."
            );
        }

        Matricula matricula = new Matricula(aluno, this);
        matriculas.add(matricula);
        aluno.adicionarMatricula(matricula); // colaboração: aluno registra no histórico
        return matricula;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OfertaDisciplina)) return false;
        OfertaDisciplina that = (OfertaDisciplina) o;
        return Objects.equals(turma, that.turma)
                && Objects.equals(disciplina, that.disciplina);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turma, disciplina);
    }

    @Override
    public String toString() {
        return disciplina.getNome() + " @ " + turma.getCodigo();
    }
}
