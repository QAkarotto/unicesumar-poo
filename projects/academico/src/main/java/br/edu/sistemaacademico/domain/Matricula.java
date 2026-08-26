package br.edu.sistemaacademico.domain;

public class Matricula {

    private Aluno aluno;
    private OfertaDisciplina oferta;
    private ResultadoAcademico resultado;

    public Matricula(Aluno aluno, OfertaDisciplina oferta) {

        if (aluno == null) {
            throw new IllegalArgumentException("Aluno é obrigatório");
        }

        if (oferta == null) {
            throw new IllegalArgumentException("Oferta é obrigatória");
        }

    private final Aluno aluno;
    private final OfertaDisciplina oferta;
    private ResultadoAcademico resultado;

    public Matricula(Aluno aluno, OfertaDisciplina oferta) {
        this.aluno = aluno;
        this.oferta = oferta;
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

    public void concluir(ResultadoAcademico resultado) {

        if (resultado == null) {
            throw new IllegalArgumentException(
                    "Resultado é obrigatório"
            );
        }

        if (this.resultado != null) {
            throw new IllegalStateException(
                    "A matrícula já foi concluída"
            );
        }

        // Aqui a matrícula muda de estado, igual uma transformação do Goku.
        if (this.resultado != null) {
            throw new IllegalStateException(
                    "Matrícula já foi concluída anteriormente com resultado: " + this.resultado
            );
        }
        this.resultado = resultado;
    }

    @Override
    public String toString() {
        return "Matricula{" +
                "aluno=" + aluno.getNome() +
                ", disciplina=" + oferta.getDisciplina().getNome() +
                ", resultado=" + resultado +
                '}';
    }
}
        return aluno.getNome()
                + " - " + oferta.getDisciplina().getNome()
                + " (" + oferta.getTurma().getCodigo() + ")"
                + " - resultado: " + (resultado == null ? "cursando" : resultado);
    }
}
