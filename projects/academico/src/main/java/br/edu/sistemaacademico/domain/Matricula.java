package br.edu.sistemaacademico.domain;

import java.util.Objects;

public class Matricula {

    private final Aluno aluno;
    private final OfertaDisciplina ofertaDisciplina;
    private ResultadoAcademico resultado;

    // Construtor package-private: só OfertaDisciplina cria Matricula
    Matricula(Aluno aluno, OfertaDisciplina ofertaDisciplina) {
        this.aluno = aluno;
        this.ofertaDisciplina = ofertaDisciplina;
        this.resultado = null; // ainda cursando
    }

    public Aluno getAluno() {
        return aluno;
    }

    public OfertaDisciplina getOfertaDisciplina() {
        return ofertaDisciplina;
    }

    public ResultadoAcademico getResultado() {
        return resultado;
    }

    public boolean isConcluida() {
        return resultado != null;
    }

    public void registrarResultado(ResultadoAcademico resultado) {
        if (this.resultado != null) {
            throw new IllegalStateException(
                "Resultado já registrado para a matrícula de " + aluno.getNome()
                + " em " + ofertaDisciplina.getDisciplina().getNome()
            );
        }
        this.resultado = resultado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Matricula)) return false;
        Matricula that = (Matricula) o;
        return Objects.equals(aluno, that.aluno)
            && Objects.equals(ofertaDisciplina, that.ofertaDisciplina);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aluno, ofertaDisciplina);
    }

    @Override
    public String toString() {
        String res = resultado != null ? resultado.toString() : "EM CURSO";
        return aluno.getNome() + " | " + ofertaDisciplina.getDisciplina().getNome()
            + " | " + ofertaDisciplina.getTurma().getCodigo()
            + " | " + res;
    }
}
