package br.edu.sistemaacademico.domain;

import java.util.Objects;

public class Disciplina {
    private final String codigo;
    private final String nome;
    private final int cargaHoraria;

    public Disciplina(String codigo, String nome, int cargaHoraria) {

        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException(
                "Código da disciplina não pode ser nulo ou vazio."
            );
        }

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException(
                "Nome da disciplina não pode ser nulo ou vazio."
            );
        }

        if (cargaHoraria <= 0) {
            throw new IllegalArgumentException(
                "Carga horária deve ser maior que zero."
            );
        }

        this.codigo = codigo.trim();
        this.nome = nome.trim();
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
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Disciplina)) {
            return false;
        }

        Disciplina that = (Disciplina) o;

        return Objects.equals(codigo, that.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return nome + " (" + codigo + ")";
    }
}
