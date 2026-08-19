package br.edu.sistemaacademico.domain;

public class Disciplina {

    private String codigo;
    private String nome;
    private int cargaHoraria;

    public Disciplina(String codigo, String nome, int cargaHoraria) {
        setCodigo(codigo);
        setNome(nome);
        setCargaHoraria(cargaHoraria);
    }

    private void setCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException(
                    "Código da disciplina não pode ser nulo ou vazio."
            );
        }

        this.codigo = codigo;
    }

    private void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException(
                    "Nome da disciplina não pode ser nulo ou vazio."
            );
        }

        this.nome = nome;
    }

    private void setCargaHoraria(int cargaHoraria) {
        if (cargaHoraria <= 0) {
            throw new IllegalArgumentException(
                    "Carga horária deve ser positiva."
            );
        }

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