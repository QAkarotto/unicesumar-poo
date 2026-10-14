package br.edu.sistemaacademico.domain;

public class Matricula {
    private String id;
    private Aluno aluno;
    private OfertaDisciplina oferta;
    private ResultadoAcademico resultado = ResultadoAcademico.CURSANDO;

    public Matricula(String id, Aluno aluno, OfertaDisciplina oferta) {
        this.id = id;
        this.aluno = aluno;
        this.oferta = oferta;
        this.resultado = ResultadoAcademico.CURSANDO;
    }

    public void concluir(ResultadoAcademico resultado) {
        this.resultado = resultado;
    }

    public String getId() {
        return id;
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

    @Override
    public String toString() {
        return "Matrícula: " + aluno.getNome() + " - " + oferta.getDisciplina().getNome();
    }
}