package br.edu.sistemaacademico.domain;

public class Disciplina {

    private final String codigo;
    private final String nome;
    private final int cargaHoraria;

    public Disciplina(String codigo, String nome, int cargaHoraria) {
        this.codigo = validarObrigatorio(codigo, "Código");
        this.nome = validarObrigatorio(nome, "Nome");

        if (cargaHoraria <= 0) {
            throw new IllegalArgumentException(
                    "A carga horária deve ser positiva."
            );
        }

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

    private String validarObrigatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                    campo + " não pode ser vazio."
            );
        }

        return valor.trim();
    }

    @Override
    public String toString() {
        return codigo + " - " + nome;
    }
}