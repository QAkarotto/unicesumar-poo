package br.edu.sistemaacademico.domain;

public class Matricula {

    private final Aluno aluno;
    private final OfertaDisciplina oferta;
    private ResultadoAcademico resultado;

    public Matricula(Aluno aluno, OfertaDisciplina oferta) {
        this.aluno = aluno;
        this.oferta = oferta;
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
        if (this.resultado != null) {
            throw new IllegalStateException(
                    "Matrícula já foi concluída anteriormente com resultado: " + this.resultado
            );
        }
        this.resultado = resultado;
    }

    @Override
    public String toString() {
        return aluno.getNome()
                + " - " + oferta.getDisciplina().getNome()
                + " (" + oferta.getTurma().getCodigo() + ")"
                + " - resultado: " + (resultado == null ? "cursando" : resultado);
    }
}