package br.edu.sistemaacademico.domain;

public class Matricula {

    private final String codigo;
    private final Aluno aluno;
    private final OfertaDisciplina ofertaDisciplina;

    private Resultado resultado;

    private static int proximoCodigo = 1;

    public Matricula(
            Aluno aluno,
            OfertaDisciplina ofertaDisciplina) {

        if (aluno == null) {
            throw new IllegalArgumentException(
                    "O aluno é obrigatório.");
        }

        if (ofertaDisciplina == null) {
            throw new IllegalArgumentException(
                    "A oferta de disciplina é obrigatória.");
        }

        this.codigo = "MAT-" + proximoCodigo++;
        this.aluno = aluno;
        this.ofertaDisciplina = ofertaDisciplina;
    }

    public String getCodigo() {
        return codigo;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public OfertaDisciplina getOfertaDisciplina() {
        return ofertaDisciplina;
    }

    public Resultado getResultado() {
        return resultado;
    }

    public void registrarResultado(
            Resultado resultado) {

        if (resultado == null) {
            throw new IllegalArgumentException(
                    "O resultado é obrigatório.");
        }

        if (this.resultado != null) {
            throw new IllegalStateException(
                    "O resultado da matrícula já foi registrado.");
        }

        this.resultado = resultado;
    }

    @Override
    public String toString() {

        String resultadoTexto =
                resultado == null
                        ? "PENDENTE"
                        : resultado.toString();

        return codigo
                + " - "
                + aluno.getNome()
                + " - "
                + ofertaDisciplina
                .getDisciplina()
                .getNome()
                + " - "
                + resultadoTexto;
    }

    // Goku provavelmente aprovaria esse encapsulamento.
}