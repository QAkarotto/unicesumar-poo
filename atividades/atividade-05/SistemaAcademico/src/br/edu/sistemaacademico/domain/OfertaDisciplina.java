package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OfertaDisciplina {
    private final String codigo;
    private final Disciplina disciplina;
    private final Turma turma;
    private final List<MatriculaDisciplina> matriculas;

    public OfertaDisciplina(String codigo, Disciplina disciplina, Turma turma) {
        validarCodigo(codigo);
        validarDisciplina(disciplina);
        validarTurma(turma);

        this.codigo = codigo;
        this.disciplina = disciplina;
        this.turma = turma;
        this.matriculas = new ArrayList<>();
    }

    private void validarCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Código da oferta de disciplina não pode ser vazio");
        }
    }

    private void validarDisciplina(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException("Disciplina não pode ser nula");
        }
    }

    private void validarTurma(Turma turma) {
        if (turma == null) {
            throw new IllegalArgumentException("Turma não pode ser nula");
        }
    }

    public String getCodigo() {
        return codigo;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public Turma getTurma() {
        return turma;
    }

    public void adicionarMatricula(MatriculaDisciplina matricula) {
        if (matricula == null) {
            throw new IllegalArgumentException("Matrícula não pode ser nula");
        }

        if (possuiMatriculaDuplicada(matricula.getAluno())) {
            throw new IllegalStateException(
                "Aluno " + matricula.getAluno().getNome() +
                " já está matriculado nesta oferta de disciplina"
            );
        }

        matriculas.add(matricula);
    }

    private boolean possuiMatriculaDuplicada(Aluno aluno) {
        return matriculas.stream()
                .anyMatch(m -> m.getAluno().getId().equals(aluno.getId()));
    }

    public List<MatriculaDisciplina> getMatriculas() {
        return Collections.unmodifiableList(matriculas);
    }

    @Override
    public String toString() {
        return "OfertaDisciplina{" +
                "codigo='" + codigo + '\'' +
                ", disciplina='" + disciplina.getNome() + '\'' +
                ", turma='" + turma.getCodigo() + '\'' +
                ", matriculas=" + matriculas.size() +
                '}';
    }
}
