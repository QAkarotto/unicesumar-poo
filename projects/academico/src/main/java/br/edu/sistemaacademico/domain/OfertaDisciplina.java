package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OfertaDisciplina {

    private final Disciplina disciplina;
    private final Turma turma;

    private final List<Matricula> matriculas = new ArrayList<>();

    public OfertaDisciplina(
            Disciplina disciplina,
            Turma turma) {

        if (disciplina == null) {
            throw new IllegalArgumentException(
                    "A disciplina é obrigatória."
            );
        }

        if (turma == null) {
            throw new IllegalArgumentException(
                    "A turma é obrigatória."
            );
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

    public List<Matricula> getMatriculas() {
        return Collections.unmodifiableList(matriculas);
    }

    public Matricula matricular(Aluno aluno) {

        if (aluno == null) {
            throw new IllegalArgumentException(
                    "O aluno é obrigatório."
            );
        }

        // Verifica se o aluno já possui aprovação nessa disciplina.
        for (Matricula matricula : aluno.getMatriculas()) {

            if (matricula.getOfertaDisciplina()
                    .getDisciplina()
                    .equals(disciplina)
                    &&
                    matricula.getResultado()
                            == ResultadoAcademico.APROVADO) {

                throw new IllegalStateException(
                        "Aluno já foi aprovado nesta disciplina."
                );
            }
        }

        // Verifica matrícula duplicada nesta oferta.
        for (Matricula matricula : matriculas) {

            if (matricula.getAluno().equals(aluno)) {
                throw new IllegalArgumentException(
                        "Aluno já está matriculado nesta oferta."
                );
            }
        }

        Matricula novaMatricula =
                new Matricula(aluno, this);

        matriculas.add(novaMatricula);

        aluno.adicionarMatricula(novaMatricula);

        return novaMatricula;
    }

    @Override
    public String toString() {
        return "OfertaDisciplina{" +
                "disciplina=" + disciplina +
                ", turma=" + turma.getCodigo() +
                '}';
    }
}