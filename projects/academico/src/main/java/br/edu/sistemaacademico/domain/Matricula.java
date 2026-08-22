package br.edu.sistemaacademico.domain;

public class Matricula {

    private final String codigo;
    private final Aluno aluno;
    private final OfertaDisciplina ofertaDisciplina;
    private ResultadoAcademico resultado;

    public Matricula(
            String codigo,
            Aluno aluno,
            OfertaDisciplina ofertaDisciplina
    ) {

        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException(
                    "Código da matrícula é obrigatório."
            );
        }

        if (aluno == null) {
            throw new IllegalArgumentException(
                    "Aluno é obrigatório."
            );
        }

        if (ofertaDisciplina == null) {
            throw new IllegalArgumentException(
                    "Oferta da disciplina é obrigatória."
            );
        }

        this.codigo = codigo.trim();
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

    public ResultadoAcademico getResultado() {
        return resultado;
    }

    public void concluir(ResultadoAcademico resultado) {

        if (this.resultado != null) {
            throw new IllegalStateException(
                    "Matrícula já foi concluída."
            );
        }

        if (resultado == null) {
            throw new IllegalArgumentException(
                    "Resultado acadêmico é obrigatório."
            );
        }

        this.resultado = resultado;
    }

    @Override
    public String toString() {
        return "Matricula{" +
                "codigo='" + codigo + '\'' +
                ", aluno=" + aluno +
                ", ofertaDisciplina=" + ofertaDisciplina +
                ", resultado=" + resultado +
                '}';
    }
}