package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;

/*
 * Representa UMA disciplina sendo ofertada por UMA turma em um período letivo.
 * É o ponto de encontro entre turma, disciplina e alunos: é ela que guarda as matrículas.
 */
public class OfertaDisciplina {

    private final Turma turma;
    private final Disciplina disciplina;
    private final List<Matricula> matriculas = new ArrayList<>();

    /*
     * Visibilidade de pacote: uma oferta só pode ser criada por Turma.ofertarDisciplina(...).
     * Isso impede que alguém crie uma oferta "solta", fora do controle da turma.
     */
    OfertaDisciplina(Turma turma, Disciplina disciplina) {
        if (turma == null) {
            throw new IllegalArgumentException("Turma da oferta é obrigatória.");
        }
        if (disciplina == null) {
            throw new IllegalArgumentException("Disciplina da oferta é obrigatória.");
        }
        this.turma = turma;
        this.disciplina = disciplina;
    }

    // ---------- Métodos de acesso ----------

    public Turma getTurma() {
        return turma;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public PeriodoLetivo getPeriodoLetivo() {
        return turma.getPeriodoLetivo();
    }

    /*
     * Cópia protegida: a lista real de matrículas nunca sai da oferta.
     */
    public List<Matricula> getMatriculas() {
        return List.copyOf(matriculas);
    }

    public boolean possuiMatriculaDe(Aluno aluno) {
        if (aluno == null) {
            return false;
        }
        return matriculas.stream()
                .anyMatch(matricula -> matricula.getAluno() == aluno);
    }

    // ---------- Regra de negócio ----------

    /*
     * A oferta valida o que ela conhece (matrícula duplicada nesta oferta)
     * e PERGUNTA ao aluno o que só ele sabe (se já foi aprovado na disciplina).
     */
    public Matricula matricular(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno é obrigatório para realizar a matrícula.");
        }
        if (possuiMatriculaDe(aluno)) {
            throw new IllegalStateException(
                    "O aluno " + aluno.getIdentificadorAcademico()
                            + " já possui matrícula na oferta " + descricaoCurta() + "."
            );
        }
        if (aluno.foiAprovadoEm(disciplina)) {
            throw new IllegalStateException(
                    "O aluno " + aluno.getIdentificadorAcademico()
                            + " já foi aprovado na disciplina " + disciplina.getCodigo()
                            + " e não pode cursá-la novamente."
            );
        }

        var matricula = new Matricula(aluno, this);
        matriculas.add(matricula);
        aluno.registrarMatricula(matricula);
        return matricula;
    }

    private String descricaoCurta() {
        return disciplina.getCodigo() + " - " + turma.getCodigo();
    }

    @Override
    public String toString() {
        return disciplina.getNome()
                + " - " + turma.getCodigo()
                + " (" + turma.getPeriodoLetivo() + ")";
    }
}