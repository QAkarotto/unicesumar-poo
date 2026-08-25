package br.edu.sistemaacademico.domain;

public class Matricula {

    private final Aluno aluno;
    private final OfertaDisciplina ofertaDisciplina;
    private Resultado resultado;

    public Matricula(Aluno aluno, OfertaDisciplina ofertaDisciplina) {
        if (aluno == null) {
            throw new IllegalArgumentException(
                    "O aluno é obrigatório."
            );
        }

        if (ofertaDisciplina == null) {
            throw new IllegalArgumentException(
                    "A oferta de disciplina é obrigatória."
            );
        }

        this.aluno = aluno;
        this.ofertaDisciplina = ofertaDisciplina;
        this.resultado = null;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public OfertaDisciplina getOfertaDisciplina() {
        return ofertaDisciplina;
    }

    public Resultado getResultado() {
        return resultado;
    }

    public void aprovar() {
        if (resultado == Resultado.APROVADO) {
            throw new IllegalStateException(
                    "A matrícula já está aprovada."
            );
        }

        resultado = Resultado.APROVADO;
    }

    public void reprovar() {
        if (resultado == Resultado.APROVADO) {
            throw new IllegalStateException(
                    "Uma matrícula aprovada não pode ser alterada."
            );
        }

        resultado = Resultado.REPROVADO;
    }

    @Override
    public String toString() {
        return "Matricula{" +
                "aluno=" + aluno.getNome() +
                ", disciplina=" +
                ofertaDisciplina.getDisciplina() +
                ", resultado=" + resultado +
                '}';
    }
}
