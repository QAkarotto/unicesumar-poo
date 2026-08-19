package br.edu.sistemaacademico.domain;

public class Disciplina {

    // O código é a identidade da disciplina no catálogo, então é imutável.
    private final String codigo;
    private String nome;
    private int cargaHoraria;

    public Disciplina(String codigo, String nome, int cargaHoraria) {
        this.codigo = validarCodigo(codigo);
        this.nome = validarNome(nome);
        this.cargaHoraria = validarCargaHoraria(cargaHoraria);
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

    // Nome e carga horária mudam quando a ementa é revisada, mas continuam
    // passando pelas mesmas regras usadas na criação.
    public void setNome(String nome) {
        this.nome = validarNome(nome);
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = validarCargaHoraria(cargaHoraria);
    }

    private static String validarCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("O código da disciplina é obrigatório.");
        }
        return codigo.strip();
    }

    private static String validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome da disciplina é obrigatório.");
        }
        return nome.strip();
    }

    private static int validarCargaHoraria(int cargaHoraria) {
        if (cargaHoraria <= 0) {
            throw new IllegalArgumentException("A carga horária deve ser positiva: " + cargaHoraria);
        }
        return cargaHoraria;
    }

    @Override
    public String toString() {
        return codigo + " - " + nome + " (" + cargaHoraria + "h)";
    }
}
