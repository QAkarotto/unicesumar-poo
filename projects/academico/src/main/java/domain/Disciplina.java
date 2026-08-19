package br.edu.sistemaacademico.domain;

public class Disciplina {

    private final String codigo;
    private final String nome;
    private final int cargaHoraria;

    public Disciplina(String codigo, String nome, int cargaHoraria) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException(
                    "O código da disciplina não pode ser vazio."
            );
        }

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException(
                    "O nome da disciplina não pode ser vazio."
            );
        }

        if (cargaHoraria <= 0) {
            throw new IllegalArgumentException(
                    "A carga horária deve ser maior que zero."
            );
        }

        this.codigo = codigo;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
    }

    @Override
    public String toString() {
        return "Disciplina{" +
                "codigo='" + codigo + '\'' +
                ", nome='" + nome + '\'' +
                ", cargaHoraria=" + cargaHoraria +
                '}';
    }
}