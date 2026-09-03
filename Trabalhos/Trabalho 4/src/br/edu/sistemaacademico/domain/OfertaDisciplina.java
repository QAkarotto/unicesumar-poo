package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;

public class OfertaDisciplina {
    private Turma turma;
    private Disciplina disciplina;
    private List<Matricula> matriculas = new ArrayList<>();

    public OfertaDisciplina(Turma turma, Disciplina disciplina) {
        this.turma = turma;
        this.disciplina = disciplina;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public List<Matricula> getMatriculas() {
        return matriculas;
    }

    public Matricula matricular(Aluno aluno) {
        // Regra 1: Aluno já aprovado na disciplina não pode se matricular de novo
        if (aluno.jaFoiAprovadoEm(this.disciplina)) {
            throw new IllegalStateException("Aluno " + aluno.getNome() + " já foi aprovado na disciplina " + disciplina.getNome() + ".");
        }

        // Regra 2: Aluno não pode ter duas matrículas na MESMA oferta
        for (Matricula m : matriculas) {
            if (m.getAluno().equals(aluno)) {
                throw new IllegalArgumentException("Aluno " + aluno.getNome() + " já está matriculado nesta oferta.");
            }
        }

        Matricula novaMatricula = new Matricula(aluno, this);
        this.matriculas.add(novaMatricula);

        // Responsabilidade dupla: adiciona no aluno também!
        aluno.adicionarMatricula(novaMatricula);

        return novaMatricula;
    }

    @Override
    public String toString() {
        return disciplina.getNome();
    }
}