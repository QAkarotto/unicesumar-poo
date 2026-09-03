public class Matricula {

    private final String numero;
    private final Aluno aluno;
    private final OfertaDisciplina ofertaDisciplina;

    private ResultadoAcademico resultado;

    public Matricula(
            String numero,
            Aluno aluno,
            OfertaDisciplina ofertaDisciplina) {

        if (numero == null || numero.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Numero da matricula obrigatorio."
            );
        }

        if (aluno == null) {
            throw new IllegalArgumentException(
                    "Aluno nao pode ser nulo."
            );
        }

        if (ofertaDisciplina == null) {
            throw new IllegalArgumentException(
                    "Oferta da disciplina obrigatoria."
            );
        }

        this.numero = numero.trim();
        this.aluno = aluno;
        this.ofertaDisciplina = ofertaDisciplina;

        /*
         * Começa sem resultado.
         */
        this.resultado = null;
    }

    public String getNumero() {
        return numero;
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

    /*
     * Conclui a matrícula com o resultado acadêmico.
     */
    public void concluir(
            ResultadoAcademico resultado) {

        if (resultado == null) {
            throw new IllegalArgumentException(
                    "Resultado obrigatorio."
            );
        }

        /*
         * Não permite concluir duas vezes.
         */
        if (this.resultado != null) {
            throw new IllegalStateException(
                    "A matricula ja foi concluida."
            );
        }

        this.resultado = resultado;
    }

    @Override
    public String toString() {

        String resultadoTexto =
                resultado == null
                        ? "EM ANDAMENTO"
                        : resultado.toString();

        return "Matricula: " + numero
                + " | Aluno: "
                + aluno.getNome()
                + " | Disciplina: "
                + ofertaDisciplina
                .getDisciplina()
                .getDescricao()
                + " | Resultado: "
                + resultadoTexto;
    }
}