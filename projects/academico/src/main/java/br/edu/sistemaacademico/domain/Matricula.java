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
        validarCodigo(codigo);
        validarAluno(aluno);
        validarOfertaDisciplina(ofertaDisciplina);

        this.codigo = codigo;
        this.aluno = aluno;
        this.ofertaDisciplina = ofertaDisciplina;
        this.resultado = null;
    }

    private void validarCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Código da matrícula não pode ser nulo ou vazio");
        }
    }

    private void validarAluno(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException(
                    "Aluno não pode ser nulo");
        }
    }

    private void validarOfertaDisciplina(
            OfertaDisciplina ofertaDisciplina
    ) {
        if (ofertaDisciplina == null) {
            throw new IllegalArgumentException(
                    "Oferta de disciplina não pode ser nula");
        }
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
                    "A matrícula já possui um resultado registrado");
        }

        if (resultado == null) {
            throw new IllegalArgumentException(
                    "Resultado acadêmico não pode ser nulo");
        }

        this.resultado = resultado;
    }

    @Override
    public String toString() {
        return String.format(
                "Matricula{codigo='%s', aluno='%s', disciplina='%s', resultado=%s}",
                codigo,
                aluno.getNome(),
                ofertaDisciplina.getDisciplina().getNome(),
                resultado
        );
    }
}