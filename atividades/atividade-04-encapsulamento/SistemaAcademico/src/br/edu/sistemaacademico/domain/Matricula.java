package br.edu.sistemaacademico.domain;

public class Matricula {
    private final String codigo;
    private final Aluno aluno;
    private final Turma turma;

    public Matricula(String codigo, Aluno aluno, Turma turma) {
        validarCodigo(codigo);
        validarAluno(aluno);
        validarTurma(turma);
        
        this.codigo = codigo;
        this.aluno = aluno;
        this.turma = turma;
    }

    private void validarCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Código da matrícula não pode ser vazio");
        }
    }

    private void validarAluno(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno não pode ser nulo");
        }
    }

    private void validarTurma(Turma turma) {
        if (turma == null) {
            throw new IllegalArgumentException("Turma não pode ser nula");
        }
    }

    public String getCodigo() {
        return codigo;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public Turma getTurma() {
        return turma;
    }

    @Override
    public String toString() {
        return "Matricula{" +
                "codigo='" + codigo + '\'' +
                ", aluno=" + aluno.getNome() +
                ", disciplina=" + turma.getDisciplina().getNome() +
                ", periodo=" + turma.getPeriodoLetivo().getAno() + 
                " " + turma.getPeriodoLetivo().getSemestre().getDescricao() +
                '}';
    }
}
