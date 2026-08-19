package br.edu.sistemaacademico;


public class Disciplina {

    private final String codigo;
    private final String nome;
    private final int cargaHoraria;

    public Disciplina(String codigo, String nome, int cargaHoraria) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("O código da disciplina é obrigatório.");
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome da disciplina é obrigatório.");
        }
        if (cargaHoraria <= 0) {
            throw new IllegalArgumentException("A carga horária da disciplina deve ser positiva.");
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

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    @Override
    public String toString() {
        return String.format("Disciplina{codigo=%s, nome=%s, cargaHoraria=%dh}",
                codigo, nome, cargaHoraria);
    }
}