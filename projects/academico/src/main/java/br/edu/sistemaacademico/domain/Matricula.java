package br.edu.sistemaacademico.domain;

public class Matricula {

    private final Aluno aluno;
    private final OfertaDisciplina oferta;

    private ResultadoAcademico resultado;

    Matricula(
            Aluno aluno,
            OfertaDisciplina oferta
    ) {
        if (aluno == null) {
            throw new IllegalArgumentException(
                    "Aluno é obrigatório."
            );
        }

        if (oferta == null) {
            throw new IllegalArgumentException(
                    "Oferta é obrigatória."
            );
        }

        this.aluno = aluno;
        this.oferta = oferta;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public OfertaDisciplina getOferta() {
        return oferta;
    }

    public Disciplina getDisciplina() {
        return oferta.getDisciplina();
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

        // Depois de concluída, nem o Kamehameha altera o resultado da matrícula.
        this.resultado = resultado;
    }

    @Override
    public String toString() {
        return aluno.getRa()
                + " - "
                + oferta.getDisciplina().getCodigo()
                + " - "
                + oferta.getTurma().getCodigo();
    }
}