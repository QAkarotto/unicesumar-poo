package br.edu.sistemaacademico.domain;

import java.util.concurrent.atomic.AtomicInteger;

public class Matricula {

    private static final AtomicInteger SEQUENCIAL = new AtomicInteger(1);

    private final String codigo;
    private final Aluno aluno;
    private final OfertaDisciplina ofertaDisciplina;
    private ResultadoAcademico resultado;

    /**
     * Construtor de pacote: uma Matrícula só pode ser criada pela
     * OfertaDisciplina à qual pertence, pois é ela quem valida as
     * regras de matrícula (duplicidade na oferta, etc). Isso evita que
     * matrículas "soltas", fora de controle, sejam criadas por
     * qualquer outra classe.
     */
    Matricula(OfertaDisciplina ofertaDisciplina, Aluno aluno) {
        if (ofertaDisciplina == null) {
            throw new IllegalArgumentException("A oferta de disciplina não pode ser nula.");
        }
        if (aluno == null) {
            throw new IllegalArgumentException("O aluno não pode ser nulo.");
        }
        this.codigo = "MAT-" + SEQUENCIAL.getAndIncrement();
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

    /**
     * Registra o resultado acadêmico da matrícula, concluindo-a.
     * Uma matrícula só pode ser concluída uma única vez: alterar o
     * resultado de uma matrícula já concluída é uma alteração de
     * estado inválida.
     */
    public void concluir(ResultadoAcademico resultado) {
        if (resultado == null) {
            throw new IllegalArgumentException("O resultado não pode ser nulo.");
        }
        if (this.resultado != null) {
            throw new IllegalStateException(
                    "A matrícula " + codigo + " já foi concluída com resultado " + this.resultado + "."
            );
        }
        this.resultado = resultado;
    }

    public boolean isConcluida() {
        return resultado != null;
    }

    @Override
    public String toString() {
        return "Matricula{" +
                "codigo='" + codigo + '\'' +
                ", aluno=" + aluno.getNome() +
                ", disciplina=" + ofertaDisciplina.getDisciplina().getNome() +
                ", turma=" + ofertaDisciplina.getTurma().getCodigo() +
                ", periodo=" + ofertaDisciplina.getTurma().getPeriodoLetivo() +
                ", resultado=" + (resultado != null ? resultado : "EM_CURSO") +
                '}';
    }
}
