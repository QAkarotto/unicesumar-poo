package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class OfertaDisciplina {

    private final Turma turma;
    private final Disciplina disciplina;
    private final List<Matricula> matriculas = new ArrayList<>();

    public OfertaDisciplina(Turma turma, Disciplina disciplina) {
        if (turma == null || disciplina == null) {
            throw new IllegalArgumentException("Turma e Disciplina não podem ser nulas.");
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

    public List<Matricula> getMatriculas() {
        return Collections.unmodifiableList(matriculas);
    }

    public Matricula matricular(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno não pode ser nulo.");
        }

        // Verifica se o aluno já foi aprovado nesta disciplina alguma vez
        if (aluno.isAprovadoNaDisciplina(this.disciplina)) {
            throw new IllegalStateException("Aluno aprovado não pode cursar novamente a mesma disciplina.");
        }

        // Verifica se o aluno já está matriculado especificamente nesta oferta (evitar duplicação)
        boolean jaMatriculado = matriculas.stream()
                .anyMatch(m -> m.getAluno().getIdentificadorAcademico().equals(aluno.getIdentificadorAcademico()));

        if (jaMatriculado) {
            throw new IllegalStateException("Aluno não pode possuir duas matrículas na mesma oferta.");
        }

        String codigoMatricula = UUID.randomUUID().toString();
        Matricula novaMatricula = new Matricula(codigoMatricula, aluno, this);

        this.matriculas.add(novaMatricula);
        aluno.adicionarMatricula(novaMatricula);

        return novaMatricula;
    }

    @Override
    public String toString() {
        return disciplina.getNome();
    }
}