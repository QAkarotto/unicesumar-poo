package br.edu.sistemaacademico.domain;

import java.util.Objects;

public class Matricula {

    private final Aluno aluno;
    private final OfertaDisciplina oferta;
    private ResultadoAcademico resultado;

    Matricula(Aluno aluno, OfertaDisciplina oferta) {
        if (aluno == null) {
            throw new IllegalArgumentException("O aluno é obrigatório.");
        }
        if (oferta == null) {
            throw new IllegalArgumentException("A oferta de disciplina é obrigatória.");
        }
        this.aluno = aluno;
        this.oferta = oferta;
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

    public Turma getTurma() {
        return oferta.getTurma();
    }

    public PeriodoLetivo getPeriodoLetivo() {
        return oferta.getPeriodoLetivo();
    }

    public ResultadoAcademico getResultado() {
        return resultado;
    }

    public void concluir(ResultadoAcademico resultado) {
        if (resultado == null) {
            throw new IllegalArgumentException("O resultado acadêmico é obrigatório.");
        }
        if (!isEmCurso()) {
            throw new IllegalStateException("A matrícula de " + aluno.getNome() + " em "
                    + oferta + " já foi concluída como " + this.resultado + ".");
        }
        this.resultado = resultado;
    }

    public boolean isEmCurso() {
        return resultado == null;
    }

    public boolean isAprovada() {
        return resultado != null && resultado.isAprovado();
    }

    boolean refereSeA(Disciplina disciplina) {
        return oferta.refereSeA(disciplina);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Matricula)) {
            return false;
        }
        Matricula outra = (Matricula) obj;
        return aluno.equals(outra.aluno) && oferta.equals(outra.oferta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aluno, oferta);
    }

    @Override
    public String toString() {
        return aluno.getNome() + " - " + oferta + " - " + (isEmCurso() ? "EM CURSO" : resultado.name());
    }
}
