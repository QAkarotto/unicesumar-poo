package br.edu.sistemaacademico.domain;

public class Disciplina {
    private final String codigo;
    private final String nome;
    private final int cargaHoraria;

    public Disciplina(String codigo, String nome, int cargaHoraria) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código da disciplina é obrigatório.");
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome da disciplina é obrigatório.");
        }
        if (cargaHoraria <= 0) {
            throw new IllegalArgumentException("Carga horária deve ser positiva.");
        }

        this.codigo = codigo.trim();
        this.nome = nome.trim();
        this.cargaHoraria = cargaHoraria;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%dh)", codigo, nome, cargaHoraria);
    }
}