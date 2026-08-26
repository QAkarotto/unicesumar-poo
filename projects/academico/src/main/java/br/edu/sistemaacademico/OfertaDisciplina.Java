package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OfertaDisciplina {
    private final Turma turma;
    private final Disciplina disciplina;
    private final List<Matricula> matriculas;

    OfertaDisciplina(Turma turma, Disciplina disciplina) {
        this.turma = turma;
        this.disciplina = disciplina;
        this.matriculas = new ArrayList<>();
    }

    public Matricula matricular(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("O aluno não pode ser nulo.");
        }

        boolean jaMatriculadoNaOferta = matriculas.stream()
                .anyMatch(m -> m.getAluno().equals(aluno));
        if (jaMatriculadoNaOferta) {
            throw new IllegalStateException("Aluno não pode possuir duas matrículas na mesma oferta.");
        }

        if (aluno.isAprovadoEm(disciplina)) {
            throw new IllegalStateException("Aluno aprovado não pode cursar novamente a mesma disciplina.");
        }

        Matricula novaMatricula = new Matricula(aluno, this);
        this.matriculas.add(novaMatricula);
        aluno.adicionarMatricula(novaMatricula);

        return novaMatricula;
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

    @Override
    public String toString() {
        return String.format("Oferta[%s na %s]", disciplina.getNome(), turma.getCodigo());
    } // João Pedro Hulchak Kazmierzak RA: 25141620-2 e Hiuri Luciano dos Santos RA: 25208360-2
}