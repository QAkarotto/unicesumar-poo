package br.edu.sistemaacademico.domain;

import java.util.Objects;

public class Matricula {
    private final Aluno aluno;
    private final OfertaDisciplina ofertaDisciplina;
    private ResultadoAcademico resultado;

    public Matricula(Aluno aluno, OfertaDisciplina ofertaDisciplina) {
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno não pode ser nulo.");
        }
        if (ofertaDisciplina == null) {
            throw new IllegalArgumentException("Oferta não pode ser nula.");
        }
        this.aluno = aluno;
        this.ofertaDisciplina = ofertaDisciplina;
        this.resultado = ResultadoAcademico.EM_ANDAMENTO;
    }

    public void concluir(ResultadoAcademico novoResultado) {
        if (novoResultado == null || novoResultado == ResultadoAcademico.EM_ANDAMENTO) {
            throw new IllegalArgumentException("O resultado de conclusão deve ser APROVADO ou REPROVADO.");
        }
        if (this.resultado != ResultadoAcademico.EM_ANDAMENTO) {
            throw new IllegalStateException("Esta matrícula já foi concluída com status: " + this.resultado);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Matricula that)) return false;
        return Objects.equals(aluno, that.aluno) && Objects.equals(ofertaDisciplina, that.ofertaDisciplina);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aluno, ofertaDisciplina);
    }

    @Override
    public String toString() {
        return "Matrícula [" + aluno.getNome() + " - " + ofertaDisciplina.getDisciplina().getNome() + " | Status: " + resultado + "]";
    }
}