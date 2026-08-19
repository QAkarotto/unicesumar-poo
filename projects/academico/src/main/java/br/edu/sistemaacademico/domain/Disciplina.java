package br.edu.sistemaacademico.domain;

public class Disciplina {

    private final String codigo;
    private String nome;
    private int cargaHoraria;

    public Disciplina(String codigo, String nome, int cargaHoraria) {
        validarTexto(codigo, "Código");
        validarTexto(nome, "Nome");
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

    public void setNome(String nome) {
        validarTexto(nome, "Nome");
        this.nome = nome;
    }

    public void setCargaHoraria(int cargaHoraria) {
        validarCargaHoraria(cargaHoraria);
        this.cargaHoraria = cargaHoraria;
    }

    private void validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " não pode ser vazio");
        }
    }

    private void validarCargaHoraria(int cargaHoraria) {
        if (cargaHoraria <= 0) {
            throw new IllegalArgumentException("Carga horária deve ser positiva");
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
