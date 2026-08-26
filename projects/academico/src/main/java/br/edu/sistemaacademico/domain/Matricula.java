package br.edu.sistemaacademico.domain;

/*
 * Vínculo entre um aluno e uma disciplina ofertada.
 * A matrícula nasce EM ANDAMENTO (sem resultado) e é concluída uma única vez.
 */
public class Matricula {

    private final Aluno aluno;
    private final OfertaDisciplina oferta;

    // Sem resultado = matrícula ainda em andamento.
    private ResultadoAcademico resultado;

    /*
     * Visibilidade de pacote: matrícula só é criada por OfertaDisciplina.matricular(...),
     * que é quem valida as regras antes de permitir o vínculo.
     */
    Matricula(Aluno aluno, OfertaDisciplina oferta) {
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno da matrícula é obrigatório.");
        }
        if (oferta == null) {
            throw new IllegalArgumentException("Oferta da matrícula é obrigatória.");
        }
        this.aluno = aluno;
        this.oferta = oferta;
    }

    // ---------- Métodos de acesso ----------

    public Aluno getAluno() {
        return aluno;
    }

    public OfertaDisciplina getOferta() {
        return oferta;
    }

    public Disciplina getDisciplina() {
        return oferta.getDisciplina();
    }

    public Turma getTurma() {
        return oferta.getTurma();
    }

    public PeriodoLetivo getPeriodoLetivo() {
        return oferta.getPeriodoLetivo();
    }

    public ResultadoAcademico getResultado() {
        return resultado;
    }

    public boolean estaConcluida() {
        return resultado != null;
    }

    public boolean estaEmAndamento() {
        return resultado == null;
    }

    // ---------- Regra de negócio ----------

    /*
     * A própria matrícula controla seu ciclo de vida:
     * em andamento -> concluída. Não existe caminho de volta nem reescrita de resultado.
     */
    public void concluir(ResultadoAcademico resultado) {
        if (resultado == null) {
            throw new IllegalArgumentException("Resultado acadêmico é obrigatório para concluir a matrícula.");
        }
        if (estaConcluida()) {
            throw new IllegalStateException(
                    "A matrícula de " + aluno.getIdentificadorAcademico()
                            + " em " + oferta.getDisciplina().getCodigo()
                            + " já foi concluída como " + this.resultado + "."
            );
        }
        this.resultado = resultado;
    }

    /*
     * Quem sabe dizer se ESTA matrícula representa aprovação em uma disciplina
     * é a própria matrícula. O aluno apenas percorre o histórico e pergunta.
     */
    boolean foiAprovadaEm(Disciplina disciplina) {
        return resultado != null
                && resultado.isAprovado()
                && oferta.getDisciplina().equals(disciplina);
    }

    @Override
    public String toString() {
        var situacao = estaConcluida() ? resultado.getDescricao() : "Em andamento";
        return aluno.getNome()
                + " -> " + oferta.getDisciplina().getCodigo()
                + " (" + oferta.getTurma().getCodigo() + ")"
                + " [" + situacao + "]";
    }
}