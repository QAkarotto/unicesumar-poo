
package br.edu.sistemaacademico.domain;

import java.util.Objects;

public class Matricula {

    private final String codigo;
    private final Aluno aluno;
    private final OfertaDisciplina oferta;
    private SituacaoMatricula situacao;
    private ResultadoAcademico resultado;

    Matricula(String codigo, Aluno aluno, OfertaDisciplina oferta) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("O código da matrícula é obrigatório.");
        }
        if (aluno == null) {
            throw new IllegalArgumentException("O aluno é obrigatório.");
        }
        if (oferta == null) {
            throw new IllegalArgumentException("A oferta de disciplina é obrigatória.");
        }

        this.codigo = codigo.trim();
        this.aluno = aluno;
        this.oferta = oferta;
        this.situacao = SituacaoMatricula.ATIVA;
    }

    public String getCodigo() {
        return codigo;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public OfertaDisciplina getOferta() {
        return oferta;
    }

    public Disciplina getDisciplina() {
        return oferta.getDisciplina();
    }

    public Turma getTurma() {
        return oferta.getTurma();
    }

    public PeriodoLetivo getPeriodoLetivo() {
        return oferta.getPeriodoLetivo();
    }

    public SituacaoMatricula getSituacao() {
        return situacao;
    }

    public ResultadoAcademico getResultado() {
        return resultado;
    }

    public void trancar() {
        if (situacao != SituacaoMatricula.ATIVA) {
            throw new IllegalStateException(
                    "A matrícula não está ativa e não pode ser trancada."
            );
        }

        situacao = SituacaoMatricula.TRANCADA;
    }

    public void cancelar() {
        if (situacao == SituacaoMatricula.CONCLUIDA) {
            throw new IllegalStateException(
                    "Uma matrícula concluída não pode ser cancelada."
            );
        }

        if (situacao == SituacaoMatricula.CANCELADA) {
            throw new IllegalStateException(
                    "A matrícula já está cancelada."
            );
        }

        situacao = SituacaoMatricula.CANCELADA;
    }

    public void reativar() {
        if (situacao != SituacaoMatricula.TRANCADA) {
            throw new IllegalStateException(
                    "Somente uma matrícula trancada pode ser reativada."
            );
        }

        situacao = SituacaoMatricula.ATIVA;
    }

    public void concluir(ResultadoAcademico resultado) {
        if (resultado == null) {
            throw new IllegalArgumentException(
                    "O resultado acadêmico é obrigatório."
            );
        }

        if (situacao != SituacaoMatricula.ATIVA
                && situacao != SituacaoMatricula.TRANCADA) {
            throw new IllegalStateException(
                    "A matrícula não pode ser concluída na situação atual: "
                            + situacao + "."
            );
        }

        if (!isEmCurso()) {
            throw new IllegalStateException(
                    "A matrícula de " + aluno.getNome()
                            + " em " + oferta
                            + " já possui um resultado acadêmico."
            );
        }

        this.resultado = resultado;
        this.situacao = SituacaoMatricula.CONCLUIDA;
    }

    public boolean isEmCurso() {
        return resultado == null
                && (situacao == SituacaoMatricula.ATIVA
                || situacao == SituacaoMatricula.TRANCADA);
    }

    public boolean isAprovada() {
        return situacao == SituacaoMatricula.CONCLUIDA
                && resultado != null
                && resultado.isAprovado();
    }

    boolean refereSeA(Disciplina disciplina) {
        return oferta.refereSeA(disciplina);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Matricula)) {
            return false;
        }

        Matricula outra = (Matricula) obj;

        return codigo.equalsIgnoreCase(outra.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo.toLowerCase());
    }

    @Override
    public String toString() {
        return codigo
                + " - "
                + aluno.getNome()
                + " - "
                + oferta
                + " - "
                + situacao;
    }
}
