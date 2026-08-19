package org.example;

    public class Disciplina {

    private final String codigo;
    private String nome;
    private int cargaHoraria;

    public Disciplina(String codigo, String nome, int cargaHoraria) {
        if (codigo == null || codigo.isEmpty()) {
            throw new IllegalArgumentException("O código da disciplina é obrigatório.");
        }
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("O nome da disciplina é obrigatório.");
        }
        if (cargaHoraria <= 0) {
            throw new IllegalArgumentException("A carga horária deve ser positiva.");
        }

        this.codigo = codigo;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
    }

    public String getCodigo() { return codigo; }
    public String getNome() { return nome; }
    public int getCargaHoraria() { return cargaHoraria; }

    @Override
    public String toString() {
        return "Disciplina [Código: " + codigo + ", Nome: " + nome + ", Carga Horária: " + cargaHoraria + "h]";
    }
}
