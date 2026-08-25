package br.edu.sistemaacademico.domain;

public class Matricula {

    private final Aluno aluno;
    private final OfertaDisciplina ofertaDisciplina;

    private ResultadoAcademico resultado;

    public Matricula(
            Aluno aluno,
            OfertaDisciplina ofertaDisciplina) {

        if (aluno == null) {
            throw new IllegalArgumentException(
                    "O aluno é obrigatório."
            );
        }

        if (ofertaDisciplina == null) {
            throw new IllegalArgumentException(
                    "A oferta da disciplina é obrigatória."
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
                    "O resultado acadêmico é obrigatório."
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
        return "Matricula{" +
                "aluno=" + aluno.getNome() +
                ", disciplina=" +
                ofertaDisciplina.getDisciplina().getNome() +
                ", turma=" +
                ofertaDisciplina.getTurma().getCodigo() +
                ", resultado=" + resultado +
                '}';
    }
}