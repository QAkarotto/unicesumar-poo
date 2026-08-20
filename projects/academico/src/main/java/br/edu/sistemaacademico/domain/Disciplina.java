package br.edu.sistemaacademico.domain;

public class Disciplina {
    private final String codigo;
    private String nome;
    private int cargaHoraria;

    public Disciplina(String codigo, String nome, int cargaHoraria) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código da disciplina obrigatório.");
        }
        this.codigo = codigo;
        setNome(nome);
        setCargaHoraria(cargaHoraria);
    }
    public String getCodigo() {
        return codigo;
    }
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Preencha o nome da disciplina.");
        }
        this.nome = nome;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        if (cargaHoraria <=0) {
            throw new IllegalArgumentException("Carga horária deve ser maior que zero");
        }
        this.cargaHoraria = cargaHoraria;
    }

    @Override
    public String toString() {
        return nome + " [" + codigo + "] - " + cargaHoraria + "h";
    }
}
