package br.edu.sistemaacademico.domain;

public class Matricula {

    private final Aluno aluno;
    private final OfertaDisciplina ofertaDisciplina;

    private ResultadoAcademico resultado;

    public Matricula(
            OfertaDisciplina ofertaDisciplina,
            Aluno aluno) {

        if (ofertaDisciplina == null) {
            throw new IllegalArgumentException(
                    "Oferta da disciplina é obrigatória."
            );
        }

        if (aluno == null) {
            throw new IllegalArgumentException(
                    "Aluno é obrigatório."
            );
        }

        this.ofertaDisciplina = ofertaDisciplina;
        this.aluno = aluno;
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

    public void registrarResultado(ResultadoAcademico resultado) {

        if (resultado == null) {
            throw new IllegalArgumentException(
                    "Resultado acadêmico é obrigatório."
            );
        }

        if (this.resultado != null) {
            throw new IllegalStateException(
                    "O resultado da matrícula já foi registrado."
            );
        }

        this.resultado = resultado;
    }

    @Override
    public String toString() {

        return "Matrícula: " +
                aluno.getNome() +
                " - " +
                ofertaDisciplina.getDisciplina().getNome() +
                " - Resultado: " +
                (resultado == null ? "PENDENTE" : resultado);
    }
}