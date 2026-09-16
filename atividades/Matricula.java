package br.edu.sistemaacademico.domain;

public class Matricula {

    private final String codigo;
    private final Aluno aluno;
    private final Turma turma;
    private SituacaoMatricula situacao;

    public Matricula(String codigo, Aluno aluno, Turma turma) {
        this.codigo = validarObrigatorio(codigo, "Código");

        if (aluno == null) {
            throw new IllegalArgumentException(
                    "O aluno não pode ser nulo."
            );
        }

        if (turma == null) {
            throw new IllegalArgumentException(
                    "A turma não pode ser nula."
            );
        }

        this.aluno = aluno;
        this.turma = turma;
        this.situacao = SituacaoMatricula.EM_CURSO;
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

    public SituacaoMatricula getSituacao() {
        return situacao;
    }

    /**
      aprova o aluno nesta matrícula.
      só é permitido a partir da situação EM_CURSO; uma matrícula já
      finalizada (aprovada ou reprovada) não pode mudar de situação.
     */
    public void aprovar() {
        if (situacao != SituacaoMatricula.EM_CURSO) {
            throw new IllegalStateException(
                    "Somente matrículas em curso podem ser aprovadas."
            );
        }

        situacao = SituacaoMatricula.APROVADO;
    }

    /**
     reprova o aluno nesta matrícula.
     só é permitido a partir da situação EM_CURSO.
     */
    public void reprovar() {
        if (situacao != SituacaoMatricula.EM_CURSO) {
            throw new IllegalStateException(
                    "Somente matrículas em curso podem ser reprovadas."
            );
        }

        situacao = SituacaoMatricula.REPROVADO;
    }

    private static String validarObrigatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                    campo + " não pode ser nulo ou vazio."
            );
        }

        return valor;
    }

    @Override
    public String toString() {

        return "Matricula{" +
                "codigo='" + codigo + '\'' +
                ", aluno=" + aluno +
                ", turma=" + turma +
                ", situacao=" + situacao +
                '}';
    }
}
