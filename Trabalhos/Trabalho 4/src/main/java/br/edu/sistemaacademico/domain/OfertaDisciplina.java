package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class OfertaDisciplina {
    private Turma turma;
    private Disciplina disciplina;
    private List<Matricula> matriculas = new ArrayList<>();
    public String getCodigo() {
        return turma.getCodigo() + "-" + disciplina.getCodigo();
    }

    public OfertaDisciplina(Turma turma, Disciplina disciplina) {
        this.turma = turma;
        this.disciplina = disciplina;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    @JsonIgnore public List<Matricula> getMatriculas() {
        return matriculas;
    }

    public Matricula matricular(Aluno aluno) {

        if (aluno.jaFoiAprovadoEm(this.disciplina)) {
            throw new IllegalStateException("Aluno " + aluno.getNome() + " já foi aprovado na disciplina " + disciplina.getNome() + ".");
        }

        // Regra 2: Aluno não pode ter duas matrículas na mesma oferta
        for (Matricula m : matriculas) {
            if (m.getAluno().equals(aluno)) {
                throw new IllegalArgumentException("Aluno " + aluno.getNome() + " já está matriculado nesta oferta.");
            }
        }

        String idMatricula = "MAT-" + aluno.getRa() + "-" + disciplina.getSigla();
        Matricula novaMatricula = new Matricula(idMatricula, aluno, this);

        this.matriculas.add(novaMatricula);
        aluno.getMatriculas().add(novaMatricula);

        return novaMatricula;
    }

    @Override
    public String toString() {
        return disciplina.getNome();
    }
}