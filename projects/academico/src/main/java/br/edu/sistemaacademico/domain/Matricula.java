package br.edu.sistemaacademico.domain;

/**
 * Representa o vínculo entre um {@link Aluno} e uma {@link OfertaDisciplina}.
 *
 * <p>Uma matrícula nasce sem resultado (ainda em andamento) e só pode ser
 * concluída uma vez, através de {@link #concluir(ResultadoAcademico)}. O
 * aluno e a oferta aos quais ela pertence não mudam durante sua vida — só o
 * resultado evolui, e apenas de "em andamento" para um valor final.</p>
 */
public class Matricula {

    private final Aluno aluno;
    private final OfertaDisciplina ofertaDisciplina;
    private ResultadoAcademico resultado;

    Matricula(Aluno aluno, OfertaDisciplina ofertaDisciplina) {
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno da matrícula é obrigatório.");
        }
        if (ofertaDisciplina == null) {
            throw new IllegalArgumentException("Oferta de disciplina da matrícula é obrigatória.");
        }

        this.aluno = aluno;
        this.ofertaDisciplina = ofertaDisciplina;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public OfertaDisciplina getOfertaDisciplina() {
        return ofertaDisciplina;
    }

    /**
     * Resultado acadêmico da matrícula, ou {@code null} se ela ainda estiver
     * em andamento (não concluída).
     */
    public ResultadoAcademico getResultado() {
        return resultado;
    }

    /**
     * Conclui a matrícula com um resultado acadêmico.
     *
     * @throws IllegalArgumentException se o resultado for nulo
     * @throws IllegalStateException    se a matrícula já tiver sido concluída antes
     */
    public void concluir(ResultadoAcademico resultado) {
        if (resultado == null) {
            throw new IllegalArgumentException("Resultado da matrícula é obrigatório.");
        }
        if (this.resultado != null) {
            throw new IllegalStateException("Matrícula já foi concluída e não pode ser alterada.");
        }

        this.resultado = resultado;
    }

    @Override
    public String toString() {
        return "Matricula{" +
                "aluno='" + aluno.getNome() + '\'' +
                ", oferta=" + ofertaDisciplina +
                ", resultado=" + (resultado == null ? "EM_ANDAMENTO" : resultado) +
                '}';
    }
}
