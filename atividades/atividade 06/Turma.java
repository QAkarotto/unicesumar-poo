package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Turma {

    private final String codigo;
    private final Disciplina disciplina;
    private final PeriodoLetivo periodoLetivo;
    private final List<Matricula> matriculas = new ArrayList<>();

    public Turma(
            String codigo,
            Disciplina disciplina,
            PeriodoLetivo periodoLetivo) {

        this.codigo = validarObrigatorio(codigo, "Código");

        if (disciplina == null) {
            throw new IllegalArgumentException(
                    "A disciplina não pode ser nula."
            );
        }

        if (periodoLetivo == null) {
            throw new IllegalArgumentException(
                    "O período letivo não pode ser nulo."
            );
        }

        this.disciplina = disciplina;
        this.periodoLetivo = periodoLetivo;
    }

    public String getCodigo() {
        return codigo;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public PeriodoLetivo getPeriodoLetivo() {
        return periodoLetivo;
    }

    public List<Matricula> getMatriculas() {
        return Collections.unmodifiableList(matriculas);
    }

    /**
     * Matricula um aluno nesta turma (oferta de disciplina em um período letivo).
     * <p>
     * Regras aplicadas:
     * <ul>
     *     <li>o aluno não pode ter, nesta turma, uma matrícula já em curso
     *     (bloqueio de matrícula duplicada);</li>
     *     <li>o aluno não pode se matricular novamente caso já tenha sido
     *     aprovado nesta turma;</li>
     *     <li>o aluno PODE se matricular novamente caso a matrícula anterior
     *     tenha resultado em reprovação.</li>
     * </ul>
     */
    public Matricula matricular(String codigoMatricula, Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException(
                    "O aluno não pode ser nulo."
            );
        }

        boolean possuiMatriculaEmCurso = matriculas.stream()
                .anyMatch(m -> mesmoAluno(m, aluno)
                        && m.getSituacao() == SituacaoMatricula.EM_CURSO);

        if (possuiMatriculaEmCurso) {
            throw new IllegalStateException(
                    "Aluno já possui matrícula em curso nesta turma."
            );
        }

        boolean jaAprovadoNestaTurma = matriculas.stream()
                .anyMatch(m -> mesmoAluno(m, aluno)
                        && m.getSituacao() == SituacaoMatricula.APROVADO);

        if (jaAprovadoNestaTurma) {
            throw new IllegalStateException(
                    "Aluno já foi aprovado nesta turma. Nova matrícula não é permitida."
            );
        }

        Matricula matricula = new Matricula(codigoMatricula, aluno, this);
        matriculas.add(matricula);
        return matricula;
    }

    private boolean mesmoAluno(Matricula matricula, Aluno aluno) {
        return matricula.getAluno().getIdentificadorAcademico()
                .equals(aluno.getIdentificadorAcademico());
    }

    private static String validarObrigatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                    campo + " não pode ser nulo ou vazio."
            );
        }

        return valor;
    }

    @Override
    public String toString() {
        return "Turma{" +
                "codigo='" + codigo + '\'' +
                ", disciplina=" + disciplina +
                ", periodoLetivo=" + periodoLetivo +
                '}';
    }
}
