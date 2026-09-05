package br.edu.sistemaacademico.domain;

public class Matricula {
    private final String codigo;
    private final Aluno aluno;
    private final OfertaDisciplina ofertaDisciplina;
    private ResultadoAcademico resultado;

    Matricula(String codigo, Aluno aluno, OfertaDisciplina ofertaDisciplina) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código da matrícula não pode ser vazio");
        }
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno não pode ser nulo");
        }
        if (ofertaDisciplina == null) {
            throw new IllegalArgumentException("Oferta de disciplina não pode ser nula");
        }

        this.codigo = codigo;
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

    public void registrarResultado(ResultadoAcademico resultado) {
        if (resultado == null) {
            throw new IllegalArgumentException("Resultado não pode ser nulo");
        }
        if (this.resultado != null) {
            throw new IllegalStateException("Resultado da matrícula já foi registrado");
        }

        this.resultado = resultado;
    }

    @Override
    public String toString() {
        return String.format("Matricula(código=%s, aluno=%s, disciplina=%s, resultado=%s)",
                codigo, aluno.getNome(), ofertaDisciplina.getDisciplina().getNome(),
                resultado == null ? "EM ANDAMENTO" : resultado);
    }
}
