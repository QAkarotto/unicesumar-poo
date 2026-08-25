package br.edu.sistemaacademico.domain;

public class Matricula {

    private final String codigo;
    private final Aluno aluno;
    private final OfertaDisciplina oferta;
    private ResultadoAcademico resultado;

    public Matricula(String codigo, Aluno aluno, OfertaDisciplina oferta) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código da matrícula não pode ser vazio");
        }
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno não pode ser nulo");
        }
        if (oferta == null) {
            throw new IllegalArgumentException("Oferta de disciplina não pode ser nula");
        }

        this.codigo = codigo;
        this.aluno = aluno;
        this.oferta = oferta;
    }

    public void concluir(ResultadoAcademico resultado) {
        if (resultado == null) {
            throw new IllegalArgumentException("O resultado acadêmico não pode ser nulo.");
        }
        if (this.resultado != null) {
            throw new IllegalStateException("Esta matrícula já possui um resultado lançado.");
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

    public ResultadoAcademico getResultado() {
        return resultado;
    }

    @Override
    public String toString() {
        return "Matricula{" +
                "aluno=" + aluno.getNome() +
                ", disciplina=" + oferta.getDisciplina().getNome() +
                ", turma=" + oferta.getTurma().getCodigo() +
                (resultado != null ? ", resultado=" + resultado : "") +
                '}';
    }
}