package br.edu.sistemaacademico.domain;

public class MatriculaDisciplina {
    private final String codigo;
    private final Aluno aluno;
    private final OfertaDisciplina oferta;
    private ResultadoMatricula resultado;

    public MatriculaDisciplina(String codigo, Aluno aluno, OfertaDisciplina oferta) {
        validarCodigo(codigo);
        validarAluno(aluno);
        validarOferta(oferta);

        this.codigo = codigo;
        this.aluno = aluno;
        this.oferta = oferta;
        this.resultado = null; // Pendente de resultado
    }

    private void validarCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Código da matrícula não pode ser vazio");
        }
    }

    private void validarAluno(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno não pode ser nulo");
        }
    }

    private void validarOferta(OfertaDisciplina oferta) {
        if (oferta == null) {
            throw new IllegalArgumentException("Oferta de disciplina não pode ser nula");
        }
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

    public ResultadoMatricula getResultado() {
        return resultado;
    }

    public void registrarResultado(ResultadoMatricula novoResultado) {
        if (novoResultado == null) {
            throw new IllegalArgumentException("Resultado não pode ser nulo");
        }

        if (resultado != null) {
            throw new IllegalStateException(
                "Resultado já foi registrado para essa matrícula: " + resultado.getDescricao()
            );
        }

        this.resultado = novoResultado;
    }

    @Override
    public String toString() {
        return "MatriculaDisciplina{" +
                "codigo='" + codigo + '\'' +
                ", aluno='" + aluno.getNome() + '\'' +
                ", disciplina='" + oferta.getDisciplina().getNome() + '\'' +
                ", turma='" + oferta.getTurma().getCodigo() + '\'' +
                ", resultado=" + (resultado != null ? resultado.getDescricao() : "Pendente") +
                '}';
    }
}
