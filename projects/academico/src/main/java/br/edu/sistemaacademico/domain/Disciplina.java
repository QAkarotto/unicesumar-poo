package br.edu.sistemaacademico.domain;

public class Disciplina {
    private final String codigo;
    private String nome;
    private int cargaHoraria;

    public Disciplina(String codigo, String nome, int cargaHoraria) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Código da disciplina é obrigatório.");
        }
        validarNome(nome);
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

    public void setNome(String nome) {
        validarNome(nome);
        this.nome = nome;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        validarCargaHoraria(cargaHoraria);
        this.cargaHoraria = cargaHoraria;
    }

    private void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da disciplina é obrigatório.");
        }
    }

    private void validarCargaHoraria(int cargaHoraria) {
        if (cargaHoraria <= 0) {
            throw new IllegalArgumentException("A carga horária deve ser positiva.");
        }
    }

    @Override
    public String toString() {
        return "Disciplina [Código=" + codigo + ", Nome=" + nome + ", Carga Horária=" + cargaHoraria + "h]";
    }
}