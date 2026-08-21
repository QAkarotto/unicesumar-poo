package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;

// É a disciplina sendo ofertada por uma turma. Como é ela que guarda as
// matrículas daquela disciplina, também é ela que matricula o aluno.
public class OfertaDisciplina {

    private final Turma turma;
    private final Disciplina disciplina;
    private final List<Matricula> matriculas = new ArrayList<>();

    // Só a turma cria oferta, por isso o construtor não é público.
    OfertaDisciplina(Turma turma, Disciplina disciplina) {
        this.turma = validarTurma(turma);
        this.disciplina = validarDisciplina(disciplina);
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

    public boolean possuiMatriculaDe(Aluno aluno) {
        if (aluno == null) {
            return false;
        }

        for (var matricula : matriculas) {
            if (matricula.getAluno().equals(aluno)) {
                return true;
            }
        }
        return false;
    }

    // A oferta responde pela matrícula repetida, que é problema dela, e
    // pergunta ao aluno sobre a aprovação, que é problema do histórico dele.
    public Matricula matricular(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("A matrícula precisa de um aluno.");
        }

        if (possuiMatriculaDe(aluno)) {
            throw new IllegalStateException("O aluno " + aluno.getIdentificadorAcademico()
                    + " já possui matrícula em " + disciplina.getCodigo()
                    + " na turma " + turma.getCodigo() + ".");
        }

        if (aluno.possuiAprovacaoEm(disciplina)) {
            throw new IllegalStateException("O aluno " + aluno.getIdentificadorAcademico()
                    + " já foi aprovado em " + disciplina.getCodigo()
                    + " e não pode cursar a disciplina novamente.");
        }

        var matricula = new Matricula(gerarCodigo(aluno), aluno, this);

        matriculas.add(matricula);
        aluno.registrarMatricula(matricula);

        return matricula;
    }

    // Não repete porque a oferta aceita uma matrícula por aluno.
    private String gerarCodigo(Aluno aluno) {
        return disciplina.getCodigo()
                + "-" + turma.getCodigo()
                + "-" + aluno.getIdentificadorAcademico();
    }

    private static Turma validarTurma(Turma turma) {
        if (turma == null) {
            throw new IllegalArgumentException("A oferta precisa de uma turma.");
        }
        return turma;
    }

    private static Disciplina validarDisciplina(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException("A oferta precisa de uma disciplina.");
        }
        return disciplina;
    }

    @Override
    public String toString() {
        return disciplina.getCodigo() + " - " + disciplina.getNome()
                + " | " + turma.getCodigo()
                + " | " + turma.getPeriodoLetivo();
    }
}
