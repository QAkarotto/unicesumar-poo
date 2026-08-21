package br.edu.sistemaacademico.domain;

// A matrícula liga o aluno a uma disciplina ofertada. Trocar um dos dois seria
// outra matrícula, então só o resultado muda depois que ela é criada.
public class Matricula {

    private final String codigo;
    private final Aluno aluno;
    private final OfertaDisciplina oferta;

    // Enquanto o resultado é nulo a matrícula está em curso.
    private ResultadoAcademico resultado;

    // Quem cria é a oferta, que já validou a matrícula antes de chegar aqui.
    Matricula(String codigo, Aluno aluno, OfertaDisciplina oferta) {
        this.codigo = validarCodigo(codigo);
        this.aluno = validarAluno(aluno);
        this.oferta = validarOferta(oferta);
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

    public Disciplina getDisciplina() {
        return oferta.getDisciplina();
    }

    public ResultadoAcademico getResultado() {
        return resultado;
    }

    public SituacaoMatricula getSituacao() {
        return resultado == null
                ? SituacaoMatricula.EM_CURSO
                : SituacaoMatricula.CONCLUIDA;
    }

    public boolean estaEmCurso() {
        return resultado == null;
    }

    // O resultado é registrado uma vez só: a matrícula protege o próprio estado.
    public void concluir(ResultadoAcademico resultado) {
        if (resultado == null) {
            throw new IllegalArgumentException("O resultado acadêmico é obrigatório para concluir a matrícula.");
        }

        if (!estaEmCurso()) {
            throw new IllegalStateException("A matrícula " + codigo
                    + " já foi concluída com o resultado " + this.resultado + ".");
        }

        this.resultado = resultado;
    }

    // Quem sabe dizer se esta matrícula encerrou a disciplina é ela mesma,
    // porque conhece o resultado e a oferta em que foi feita.
    public boolean aprovouEm(Disciplina disciplina) {
        return resultado == ResultadoAcademico.APROVADO
                && getDisciplina().equals(disciplina);
    }

    private static String validarCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("O código da matrícula é obrigatório.");
        }
        return codigo.strip();
    }

    private static Aluno validarAluno(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("A matrícula precisa de um aluno.");
        }
        return aluno;
    }

    private static OfertaDisciplina validarOferta(OfertaDisciplina oferta) {
        if (oferta == null) {
            throw new IllegalArgumentException("A matrícula precisa de uma disciplina ofertada.");
        }
        return oferta;
    }

    @Override
    public String toString() {
        if (estaEmCurso()) {
            return codigo + " | " + aluno.getNome() + " | " + getSituacao();
        }
        return codigo + " | " + aluno.getNome() + " | " + resultado;
    }
}
