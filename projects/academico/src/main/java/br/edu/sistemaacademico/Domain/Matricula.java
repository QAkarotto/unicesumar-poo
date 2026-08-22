package br.edu.sistemaacademico.domain;

public class Matricula {

    private final Aluno aluno;
    private final OfertaDisciplina ofertaDisciplina;
    private ResultadoAcademico resultado;

    Matricula(Aluno aluno, OfertaDisciplina ofertaDisciplina) {
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno é obrigatório.");
        }
        if (ofertaDisciplina == null) {
            throw new IllegalArgumentException("Oferta de disciplina é obrigatória.");
        }
        this.aluno = aluno;
        this.ofertaDisciplina = ofertaDisciplina;
    }

    public void registrarResultado(ResultadoAcademico resultado) {
        if (resultado == null) {
            throw new IllegalArgumentException("Resultado é obrigatório.");
        }
        if (this.resultado != null) {
            throw new IllegalStateException("Resultado desta matrícula já foi registrado.");
        }
        this.resultado = resultado;
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

    @Override
    public String toString() {
        var status = resultado == null ? "EM ANDAMENTO" : resultado.toString();
        return aluno + " em " + ofertaDisciplina + " [" + status + "]";
    }
}