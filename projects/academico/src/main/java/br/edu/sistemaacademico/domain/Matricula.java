package br.edu.sistemaacademico.domain;

public class Matricula {

    private final Aluno aluno;
    private final OfertaDisciplina ofertaDisciplina;
    private ResultadoAcademico resultado;

    public Matricula(
            Aluno aluno,
            OfertaDisciplina ofertaDisciplina
    ) {
        if (aluno == null) {
            throw new IllegalArgumentException(
                    "Aluno é obrigatório."
            );
        }

        if (ofertaDisciplina == null) {
            throw new IllegalArgumentException(
                    "Oferta de disciplina é obrigatória."
            );
        }

        this.aluno = aluno;
        this.ofertaDisciplina = ofertaDisciplina;
        this.resultado = null;
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

    public void concluir(
            ResultadoAcademico resultado
    ) {
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

        this.resultado = resultado;
    }

    public boolean foiAprovada() {
        return resultado == ResultadoAcademico.APROVADO;
    }

    public boolean foiReprovada() {
        return resultado == ResultadoAcademico.REPROVADO;
    }

    @Override
    public String toString() {
        return "Matrícula{"
                + "aluno=" + aluno
                + ", disciplina="
                + ofertaDisciplina.getDisciplina().getCodigo()
                + ", resultado=" + resultado
                + '}';
    }
}