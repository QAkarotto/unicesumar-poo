package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;

public class OfertaDisciplina {

    private final Turma turma;
    private final Disciplina disciplina;
    private final List<Matricula> matriculas;

    public OfertaDisciplina(Turma turma, Disciplina disciplina) {

        if (turma == null) {
            throw new IllegalArgumentException(
                    "A turma é obrigatória."
            );
        }

        if (disciplina == null) {
            throw new IllegalArgumentException(
                    "A disciplina é obrigatória."
            );
        }

        this.turma = turma;
        this.disciplina = disciplina;
        this.matriculas = new ArrayList<>();
    }

    public Matricula matricular(String codigo, Aluno aluno) {

        if (aluno == null) {
            throw new IllegalArgumentException(
                    "O aluno é obrigatório."
            );
        }

        // Verifica se o aluno já está matriculado nesta oferta
        for (Matricula matricula : matriculas) {

            if (matricula.getAluno()
                    .getIdentificadorAcademico()
                    .equals(aluno.getIdentificadorAcademico())) {

                throw new IllegalStateException(
                        "O aluno já está matriculado nesta oferta."
                );
            }
        }

        // Verifica se o aluno já foi aprovado nesta disciplina
        if (aluno.jaFoiAprovado(disciplina)) {

            throw new IllegalStateException(
                    "O aluno já foi aprovado nesta disciplina."
            );
        }

        Matricula novaMatricula =
                new Matricula(codigo, aluno, this);

        matriculas.add(novaMatricula);

        aluno.adicionarMatricula(novaMatricula);

        return novaMatricula;
    }

    public Turma getTurma() {
        return turma;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public List<Matricula> getMatriculas() {
        return matriculas;
    }
}