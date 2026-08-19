package br.edu.sistemaacademico.domain;

// A matrícula registra o vínculo entre um aluno e uma turma.
// Mudar qualquer um dos dois seria outra matrícula, por isso tudo é final.
public class Matricula {

    private final String codigo;
    private final Aluno aluno;
    private final Turma turma;

    public Matricula(String codigo, Aluno aluno, Turma turma) {
        this.codigo = validarCodigo(codigo);
        this.aluno = validarAluno(aluno);
        this.turma = validarTurma(turma);
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

    private static String validarCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("O código da matrícula é obrigatório.");
        }
        return codigo.strip();
    }

    private static Aluno validarAluno(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("A matrícula precisa de um aluno.");
        }
        return aluno;
    }

    private static Turma validarTurma(Turma turma) {
        if (turma == null) {
            throw new IllegalArgumentException("A matrícula precisa de uma turma.");
        }
        return turma;
    }

    @Override
    public String toString() {
        return codigo + " | Aluno: " + aluno.getNome()
                + " (" + aluno.getIdentificadorAcademico() + ")"
                + " | Turma: " + turma.getCodigo();
    }
}
