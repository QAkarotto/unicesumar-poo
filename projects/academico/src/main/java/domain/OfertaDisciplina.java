package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa uma Disciplina sendo ofertada por uma Turma em um
 * determinado período letivo.
 *
 * É a OfertaDisciplina quem mantém as matrículas realizadas nela e
 * quem coordena a criação de uma nova matrícula, colaborando com o
 * Aluno (que sabe informar se já foi aprovado na disciplina) para
 * decidir se a matrícula é permitida.
 */
public class OfertaDisciplina {

    private final Turma turma;
    private final Disciplina disciplina;
    private final List<Matricula> matriculas = new ArrayList<>();

    /**
     * Construtor de pacote: uma oferta só é criada por uma Turma,
     * através de ofertarDisciplina(), que já garante a ausência de
     * duplicidade antes de instanciar a oferta.
     */
    OfertaDisciplina(Turma turma, Disciplina disciplina) {
        if (turma == null) {
            throw new IllegalArgumentException("A turma não pode ser nula.");
        }
        if (disciplina == null) {
            throw new IllegalArgumentException("A disciplina não pode ser nula.");
        }
        this.turma = turma;
        this.disciplina = disciplina;
    }

    public Turma getTurma() {
        return turma;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public List<Matricula> getMatriculas() {
        return Collections.unmodifiableList(matriculas);
    }

    /**
     * Matricula um aluno nesta oferta de disciplina.
     * <p>
     * Regras aplicadas, em colaboração com o Aluno:
     * - o aluno não pode já ter sido aprovado nesta disciplina
     * (em qualquer turma/período — quem responde isso é o próprio
     * Aluno, consultando seu histórico);
     * - o aluno não pode ter mais de uma matrícula ativa nesta
     * mesma oferta.
     */
    public Matricula matricular(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("O aluno não pode ser nulo.");
        }

        if (aluno.jaAprovadoEm(disciplina)) {
            throw new IllegalStateException(
                    "O aluno " + aluno.getNome() + " já foi aprovado em "
                            + disciplina.getNome() + " e não pode se matricular novamente."
            );
        }

        boolean jaMatriculado = matriculas.stream()
                .anyMatch(m -> m.getAluno().equals(aluno));
        if (jaMatriculado) {
            throw new IllegalStateException(
                    "O aluno " + aluno.getNome() + " já está matriculado nesta oferta de "
                            + disciplina.getNome() + "."
            );
        }

        Matricula matricula = new Matricula(this, aluno);
        matriculas.add(matricula);
        aluno.registrarMatricula(matricula);
        return matricula;
    }

    @Override
    public String toString() {
        return disciplina.getCodigo() + " - " + disciplina.getNome();
    }
}
