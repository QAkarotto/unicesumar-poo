package br.edu.sistemaacademico.domain;

public class Disciplina {

    private final String codigo;
    private String nome;
    private int cargaHoraria;

    public Disciplina(String codigo, String nome, int cargaHoraria) {
        validarTexto(codigo, "O código da disciplina é obrigatório.");
        validarTexto(nome, "O nome da disciplina é obrigatório.");
        validarCargaHoraria(cargaHoraria);

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

    public void alterarNome(String nome) {
        validarTexto(nome, "O nome da disciplina é obrigatório.");
        this.nome = nome;
    }

    public void alterarCargaHoraria(int cargaHoraria) {
        validarCargaHoraria(cargaHoraria);
        this.cargaHoraria = cargaHoraria;
    }

    private void validarTexto(String valor, String mensagem) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensagem);
        }
    }

    private void validarCargaHoraria(int cargaHoraria) {
        if (cargaHoraria <= 0) {
            throw new IllegalArgumentException(
                    "A carga horária deve ser maior que zero."
            );
        }
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