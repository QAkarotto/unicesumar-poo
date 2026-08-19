package br.edu.sistemaacademico.domain;

public class Matricula {

    private final String codigo;
    private final Aluno aluno;
    private final OfertaDisciplina oferta;

    private Resultado resultado;

    public Matricula(
            String codigo,
            Aluno aluno,
            OfertaDisciplina oferta
    ) {

        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException(
                    "O código da matrícula é obrigatório."
            );
        }

        if (aluno == null) {
            throw new IllegalArgumentException(
                    "O aluno da matrícula é obrigatório."
            );
        }

        if (oferta == null) {
            throw new IllegalArgumentException(
                    "A oferta da matrícula é obrigatória."
            );
        }

        this.codigo = codigo;
        this.aluno = aluno;
        this.oferta = oferta;
        this.resultado = null;
    }

    public void registrarResultado(Resultado resultado) {

        if (resultado == null) {
            throw new IllegalArgumentException(
                    "O resultado é obrigatório."
            );
        }

        if (this.resultado != null) {
            throw new IllegalStateException(
                    "O resultado já foi registrado."
            );
        }

        this.resultado = resultado;
    }

    public String getCodigo() {
        return codigo;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public OfertaDisciplina getOferta() {
        return oferta;
    }

    public Resultado getResultado() {
        return resultado;
    }

    @Override
    public String toString() {
        return "Matricula{" +
                "codigo='" + codigo + '\'' +
                ", aluno=" + aluno.getNome() +
                ", disciplina=" + oferta.getDisciplina().getNome() +
                ", resultado=" + resultado +
                '}';
    }
}