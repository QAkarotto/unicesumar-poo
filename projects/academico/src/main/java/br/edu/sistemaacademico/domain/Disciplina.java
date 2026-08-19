package br.edu.sistemaacademico.domain;

public class Disciplina {
    private String codigo;
    private String nome;
    private int cargaHoraria;

    public Disciplina(String codigo, String nome, int cargaHoraria) {
        if (codigo == null || codigo.equals("")) {
            throw new IllegalArgumentException("O código da disciplina é obrigatório.");
        }
        if (nome == null || nome.equals("")) {
            throw new IllegalArgumentException("O nome da disciplina é obrigatório.");
        }
        if (cargaHoraria <= 0) {
            throw new IllegalArgumentException("A carga horária deve ser maior que zero.");
        }
        this.codigo = codigo;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
    }
    public String getCodigo() {
        return this.codigo;
    }
    public String getNome() {
        return this.nome;
    }
    public int getCargaHoraria() {
        return this.cargaHoraria;
    }

    @Override
    public String toString() {
        return "[" + this.codigo + "] " + this.nome + " (" + this.cargaHoraria + "h)";
    }
}