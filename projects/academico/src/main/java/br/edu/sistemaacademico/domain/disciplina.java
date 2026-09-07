package br.edu.sistemaacademico.domain;

public class Disciplina {

    private final String codigo;
    private final String nome;

    public Disciplina(String codigo, String nome) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "O código da disciplina é obrigatório."
            );
        }

        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "O nome da disciplina é obrigatório."
            );
        }

        this.codigo = codigo.trim();
        this.nome = nome.trim();
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Disciplina)) {
            return false;
        }

        Disciplina outra = (Disciplina) o;

        return codigo.equals(outra.codigo);
    }

    @Override
    public int hashCode() {
        return codigo.hashCode();
    }

    @Override
    public String toString() {
        return "Disciplina{" +
                "codigo='" + codigo + '\'' +
                ", nome='" + nome + '\'' +
                '}';
    }
}
