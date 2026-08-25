package br.edu.sistemaacademico.domain;

public class Matricula {

    private final Aluno aluno;
    private final OfertaDisciplina ofertaDisciplina;

    private ResultadoAcademico resultado;

    Matricula(
            Aluno aluno,
            OfertaDisciplina ofertaDisciplina
    ) {
        if (aluno == null) {
            throw new IllegalArgumentException(
                    "O aluno não pode ser nulo."
            );
        }

        if (ofertaDisciplina == null) {
            throw new IllegalArgumentException(
                    "A oferta da disciplina não pode ser nula."
            );
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

    public ResultadoAcademico getResultado() {
        return resultado;
    }

    public void concluir(ResultadoAcademico resultado) {
        if (resultado == null) {
            throw new IllegalArgumentException(
                    "O resultado acadêmico não pode ser nulo."
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
        return "Matrícula{"
                + "aluno=" + aluno.getNome()
                + ", disciplina="
                + ofertaDisciplina.getDisciplina().getNome()
                + ", resultado=" + resultado
                + '}';
    }
}