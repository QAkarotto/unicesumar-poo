package br.edu.sistemaacademico.domain;

import java.util.Objects;

public class Disciplina {
    private final String codigo;
    private final String nome;
    private final int cargaHoraria;

    public Disciplina(String codigo, String nome, int cargaHoraria) {
        this.codigo = validarTexto(codigo, "Código da disciplina");
        this.nome = validarTexto(nome, "Nome da disciplina");
        if (cargaHoraria <= 0) {
            throw new IllegalArgumentException("A carga horária deve ser positiva.");
        }
        this.cargaHoraria = cargaHoraria;
    }

    private static String validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " é obrigatório.");
        }
        return valor.trim();
    }

    public String getCodigo() { return codigo; }
    public String getNome() { return nome; }
    public int getCargaHoraria() { return cargaHoraria; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Disciplina outra)) return false;
        return codigo.equalsIgnoreCase(outra.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo.toUpperCase());
    }

    @Override
    public String toString() {
        return codigo + " - " + nome;
    }
}
