package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma {@link Disciplina} sendo ofertada por uma {@link Turma}.
 *
 * <p>É a oferta quem mantém as matrículas feitas nela, e quem garante que
 * um mesmo aluno não se matricule duas vezes na mesma oferta. A regra sobre
 * o aluno já ter sido aprovado antes na disciplina (em qualquer turma ou
 * período) é consultada ao próprio {@link Aluno}, que é quem guarda esse
 * histórico.</p>
 */
public class OfertaDisciplina {

    private final Disciplina disciplina;
    private final Turma turma;
    private final List<Matricula> matriculas = new ArrayList<>();

    OfertaDisciplina(Disciplina disciplina, Turma turma) {
        if (disciplina == null) {
            throw new IllegalArgumentException("Disciplina da oferta é obrigatória.");
        }
        if (turma == null) {
            throw new IllegalArgumentException("Turma da oferta é obrigatória.");
        }

        this.disciplina = disciplina;
        this.turma = turma;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public Turma getTurma() {
        return turma;
    }

    /**
     * Matricula um aluno nesta oferta.
     *
     * @throws IllegalArgumentException se o aluno for nulo
     * @throws IllegalStateException    se o aluno já estiver matriculado nesta oferta,
     *                                   ou se já tiver sido aprovado antes nesta disciplina
     */
    public Matricula matricular(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno a ser matriculado é obrigatório.");
        }
        if (possuiMatriculaDe(aluno)) {
            throw new IllegalStateException(
                    "O aluno " + aluno.getIdentificadorAcademico() + " já possui matrícula nesta oferta.");
        }
        if (aluno.jaFoiAprovadoEm(disciplina)) {
            throw new IllegalStateException(
                    "O aluno " + aluno.getIdentificadorAcademico()
                            + " já foi aprovado em " + disciplina.getCodigo()
                            + " e não pode se matricular novamente.");
        }

        var matricula = new Matricula(aluno, this);
        matriculas.add(matricula);
        aluno.registrarMatricula(matricula);
        return matricula;
    }

    /**
     * Retorna as matrículas realizadas nesta oferta (cópia somente leitura).
     */
    public List<Matricula> getMatriculas() {
        return List.copyOf(matriculas);
    }

    private boolean possuiMatriculaDe(Aluno aluno) {
        return matriculas.stream()
                .anyMatch(matricula -> matricula.getAluno() == aluno);
    }

    @Override
    public String toString() {
        return disciplina.getCodigo() + " (" + turma.getCodigo() + ")";
    }
}
