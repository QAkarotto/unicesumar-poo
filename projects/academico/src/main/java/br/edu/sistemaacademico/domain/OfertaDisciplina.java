package br.edu.sistemaacademico.domain;
import java.util.ArrayList;
import java.util.List;

public class OfertaDisciplina {
    private final Disciplina disciplina;
    private final Turma turma;
    private final List<Matricula> matriculas = new ArrayList<>();

    public OfertaDisciplina(Turma turma, Disciplina disciplina) {
        if (turma == null) {
            throw new IllegalArgumentException("A turma é obrigatória.");
        }
        if (disciplina == null) {
            throw new IllegalArgumentException("A disciplina é obrigatória.");
        }
        this.turma = turma;
        this.disciplina = disciplina;
    }

    public Matricula matricular(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("O aluno é obrigatório.");
        }
        if (aluno.jaFoiAprovadoEm(this.disciplina)) {
            throw new IllegalStateException(
                    "Aluno aprovado não pode cursar novamente a mesma disciplina."
            );
        }
        for (Matricula matricula : this.matriculas) {
            if (matricula.getAluno().getRa().equals(aluno.getRa())) {
                throw new IllegalStateException(
                        "Aluno não pode possuir duas matrículas na mesma oferta."
                );
            }
        }

        String codigoMatricula = "M" + String.format("%04d", Matricula.proximoNum());
        Matricula matricula = new Matricula(codigoMatricula, aluno, this);
        this.matriculas.add(matricula);
        aluno.adicionarMatricula(matricula);
        return matricula;
    }

    public Turma getTurma() {
        return this.turma;
    }

    public Disciplina getDisciplina() {
        return this.disciplina;
    }
//copia da lista
    public List<Matricula> getMatriculas() {
        return new ArrayList<>(this.matriculas);
    }
    @Override
    public String toString() {
        return this.disciplina.getNome();
    }
}

