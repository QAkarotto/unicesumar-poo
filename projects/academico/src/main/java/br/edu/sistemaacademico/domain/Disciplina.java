package br.edu.sistemaacademico.domain;

import java.util.Objects;

public class Disciplina {

    private String codigo;
    private String nome;
    private int cargaHoraria;

    public Disciplina(String codigo, String nome, int cargaHoraria) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código da disciplina é obrigatório");
        }

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome da disciplina é obrigatório");
        }

        if (cargaHoraria <= 0) {
            throw new IllegalArgumentException("Carga horária deve ser positiva");
        }

    private final String codigo;
    private final String nome;
    private final int cargaHoraria;

    public Disciplina(String codigo, String nome, int cargaHoraria) {
        this.codigo = codigo;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Disciplina)) {
            return false;
        }

        Disciplina outra = (Disciplina) obj;

        return Objects.equals(codigo, outra.codigo);
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Disciplina)) return false;
        Disciplina that = (Disciplina) o;
        return codigo.equals(that.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return codigo + " - " + nome;
    }
}
        return nome;
    }
}
