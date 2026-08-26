package br.edu.sistemaacademico.domain;

public class Matricula {
    private final Aluno aluno;
    private final OfertaDisciplina oferta;
    private ResultadoAcademico resultado;

    Matricula(Aluno aluno, OfertaDisciplina oferta) {
        if (aluno == null) {
            throw new IllegalArgumentException("O aluno é obrigatório.");
        }
        if (oferta == null) {
            throw new IllegalArgumentException("A oferta de disciplina é obrigatória.");
        }
        this.aluno = aluno;
        this.oferta = oferta;
    }

    public void concluir(ResultadoAcademico resultado) {
        if (resultado == null) {
            throw new IllegalArgumentException("O resultado acadêmico é obrigatório.");
        }
        if (this.resultado != null) {
            throw new IllegalStateException("A matrícula já foi concluída.");
        }
        this.resultado = resultado;
    }

    public Aluno getAluno() { return aluno; }
    public OfertaDisciplina getOferta() { return oferta; }
    public ResultadoAcademico getResultado() { return resultado; }

    @Override
    public String toString() {
        String situacao = resultado == null ? "EM CURSO" : resultado.toString();
        return aluno.getIdentificadorAcademico()
                + " - " + oferta.getDisciplina().getCodigo()
                + " - " + oferta.getTurma().getPeriodoLetivo()
                + " - " + situacao;
    }
}