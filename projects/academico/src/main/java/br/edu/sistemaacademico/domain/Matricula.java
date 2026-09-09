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
        validarTexto(codigo, "Código");

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

        this.codigo = codigo;
        this.aluno = aluno;
        this.ofertaDisciplina = ofertaDisciplina;
        this.resultado = null;
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

    public Disciplina getDisciplina() {
        return ofertaDisciplina.getDisciplina();
    }

    public Turma getTurma() {
        return ofertaDisciplina.getTurma();
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

    private static void validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                    campo + " não pode ser nulo ou vazio."
            );
        }
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
