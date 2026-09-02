package br.edu.sistemaacademico.domain;

public class Matricula {
    private final Aluno aluno;
    private final OfertaDisciplina ofertaDisciplina;
    private ResultadoAcademico resultado;

    Matricula(Aluno aluno, OfertaDisciplina ofertaDisciplina) {
        this.aluno = aluno;
        this.ofertaDisciplina = ofertaDisciplina;
    }

    public void concluir(ResultadoAcademico novoResultado) {
        if (novoResultado == null) {
            throw new IllegalArgumentException("O resultado não pode ser nulo.");
        }
        if (this.resultado == ResultadoAcademico.APROVADO) {
            throw new IllegalStateException("Matrícula já concluída com aprovação não pode ser alterada.");
        }
        this.resultado = novoResultado;
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

    @Override
    public String toString() {
        String res = resultado != null ? resultado.name() : "CURSANDO";
        return String.format("Matrícula[%s - %s - Situação: %s]", aluno.getNome(), ofertaDisciplina.getDisciplina().getNome(), res);
    } // João Pedro Hulchak Kazmierzak RA: 25141620-2 e Hiuri Luciano dos Santos RA: 25208360-2
}