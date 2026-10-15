package br.edu.sistemaacademico.domain;

import java.util.Objects;

public class Disciplina {

    private final String codigo;
    private final String nome;
    private final int cargaHoraria;

    public Disciplina(String codigo, String nome, int cargaHoraria) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código inválido.");
        }

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome inválido.");
        }

        if (cargaHoraria <= 0) {
            throw new IllegalArgumentException(
                    "A carga horária deve ser maior que zero."
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
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }

        if (!(objeto instanceof Disciplina)) {
            return false;
        }

        Disciplina outra = (Disciplina) objeto;
        return codigo.equals(outra.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return "Disciplina: " + codigo
                + " - " + nome
                + " (" + cargaHoraria + " horas)";
    }
}