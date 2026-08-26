package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;

public class OfertaDisciplina {

    private static int contadorMatriculas = 1;

    private final String codigo;
    private final Disciplina disciplina;
    private final Turma turma;
    private final List<Matricula> matriculas = new ArrayList<>();

    OfertaDisciplina(Disciplina disciplina, Turma turma) {
        if (disciplina == null) {
            throw new IllegalArgumentException("A disciplina é obrigatória.");
        }

        if (turma == null) {
            throw new IllegalArgumentException("A turma é obrigatória.");
        }

        this.disciplina = disciplina;
        this.turma = turma;
        this.codigo = turma.getCodigo() + "-" + disciplina.getCodigo();
    }

    public String getCodigo() {
        return codigo;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public Turma getTurma() {
        return turma;
    }

    // A oferta é quem mantém suas próprias matrículas e quem garante que um aluno não se matricule duas vezes na mesma oferta.


    public Matricula matricular(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("O aluno é obrigatório.");
        }

        for (Matricula matricula : matriculas) {
            if (matricula.getAluno().equals(aluno)) {
                throw new IllegalStateException(
                        "O aluno " + aluno.getNome() + " já está matriculado nesta oferta."
                );
            }
        }

        if (aluno.foiAprovadoEm(disciplina)) {
            throw new IllegalStateException(
                    "O aluno " + aluno.getNome() + " já foi aprovado em "
                            + disciplina.getNome() + " e não pode se matricular novamente."
            );
        }

        var matricula = new Matricula(
                codigo + "-" + contadorMatriculas,
                aluno,
                this
        );

        contadorMatriculas = contadorMatriculas + 1;

        matriculas.add(matricula);
        aluno.registrarMatricula(matricula);

        return matricula;
    }

    public List<Matricula> getMatriculas() {
        List<Matricula> copia = new ArrayList<>();

        for (Matricula matricula : matriculas) {
            copia.add(matricula);
        }

        return copia;
    }

    @Override
    public String toString() {
        return disciplina.getNome();
    }
}