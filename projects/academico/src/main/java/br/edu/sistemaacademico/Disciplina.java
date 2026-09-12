package br.edu.sistemaacademico;

import java.util.Objects;

public class Disciplina {
    private final String codigo;
    private final String nome;
    private final int cargaHoraria;

    public Disciplina(String codigo, String nome, int cargaHoraria) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("O código não pode ser nulo ou vazio.");
        }
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome não pode ser nulo ou vazio.");
        }
        if (cargaHoraria <= 0) {
            throw new IllegalArgumentException("A carga horária deve ser positiva.");
        }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disciplina that = (Disciplina) o;
        return Objects.equals(codigo, that.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", nome, codigo);
    } // João Pedro Hulchak Kazmierzak RA: 25141620-2 e Hiuri Luciano dos Santos RA: 25208360-2
}