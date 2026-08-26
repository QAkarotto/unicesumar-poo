package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Aluno {

    private final String ra;
    private final String nome;
    private final List<Matricula> historico;

    public Aluno(String ra, String nome) {
        this.ra = ra;
        this.nome = nome;
        this.historico = new ArrayList<>();
    }

    public String getRa() {
        return ra;
    }

    public String getNome() {
        return nome;
    }

    // Chamado por Matricula ao ser criada — o aluno registra no seu histórico
    void adicionarMatricula(Matricula matricula) {
        historico.add(matricula);
    }

    public List<Matricula> getHistorico() {
        return Collections.unmodifiableList(historico);
    }

    /**
     * Verifica se o aluno já foi APROVADO em uma disciplina específica.
     * Impede nova matrícula mesmo em outra turma ou período.
     */
    public boolean foiAprovadoEm(Disciplina disciplina) {
        return historico.stream()
                .filter(m -> m.getOfertaDisciplina().getDisciplina().equals(disciplina))
                .anyMatch(m -> m.getResultado() == ResultadoAcademico.APROVADO);
    }

    /**
     * Verifica se o aluno já está matriculado (sem resultado ainda) em uma oferta específica.
     */
    public boolean estaMatriculadoEm(OfertaDisciplina oferta) {
        return historico.stream()
                .anyMatch(m -> m.getOfertaDisciplina().equals(oferta) && m.getResultado() == null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Aluno)) return false;
        Aluno that = (Aluno) o;
        return Objects.equals(ra, that.ra);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ra);
    }

    @Override
    public String toString() {
        return nome + " [RA: " + ra + "]";
    }
}
