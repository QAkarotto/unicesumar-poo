package br.edu.sistemaacademico.domain;

public class Matricula {
    private Aluno aluno;
    private OfertaDisciplina oferta;
    private String resultado;

    public Matricula(Aluno aluno, OfertaDisciplina oferta) {
        this.aluno = aluno;
        this.oferta = oferta;
        this.resultado = ResultadoAcademico.CURSANDO;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public OfertaDisciplina getOferta() {
        return oferta;
    }

    public String getResultado() {
        return resultado;
    }

    public void concluir(String resultado) {
        this.resultado = resultado;
    }

    @Override
    public String toString() {
        return "Matrícula{" +
                "aluno=" + aluno.getNome() +
                ", oferta=" + oferta.getDisciplina().getNome() +
                ", resultado=" + resultado +
                '}';
    }
}