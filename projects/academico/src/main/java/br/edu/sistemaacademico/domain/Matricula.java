package br.edu.sistemaacademico.domain;

public class Matricula {

    private Aluno aluno;
    private OfertaDisciplina oferta;
    private ResultadoAcademico resultado;

    public Matricula(Aluno aluno, OfertaDisciplina oferta) {

        if (aluno == null) {
            throw new IllegalArgumentException("Aluno é obrigatório.");
        }

        if (oferta == null) {
            throw new IllegalArgumentException(
                    "Oferta de disciplina é obrigatória."
            );
        }

        this.aluno = aluno;
        this.oferta = oferta;
        this.resultado = null;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public OfertaDisciplina getOferta() {
        return oferta;
    }

    public ResultadoAcademico getResultado() {
        return resultado;
    }

    public void concluir(ResultadoAcademico resultado) {

        if (resultado == null) {
            throw new IllegalArgumentException(
                    "Resultado acadêmico é obrigatório."
            );
        }

        if (this.resultado != null) {
            throw new IllegalStateException(
                    "A matrícula já foi concluída."
            );
        }

        this.resultado = resultado;
    }

    @Override
    public String toString() {

        String situacao =
                resultado == null
                        ? "EM ANDAMENTO"
                        : resultado.toString();

        return aluno + " - "
                + oferta.getDisciplina()
                + " - "
                + situacao;
    }
}