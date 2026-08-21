package br.edu.sistemaacademico.domain;

public class Disciplina {

    private final String codigo;
    private String nome;
    private int cargaHoraria;

    public Disciplina(String codigo, String nome, int cargaHoraria) {
        this.codigo = validarObrigatorio(codigo, "Código");
        this.nome = validarObrigatorio(nome, "Nome");
        this.cargaHoraria = validarCargaHoraria(cargaHoraria);
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = validarObrigatorio(nome, "Nome");
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
       
        this.cargaHoraria = validarCargaHoraria(cargaHoraria);
    }

    private static String validarObrigatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                    campo + " é obrigatório."
            );
        }

        return valor.trim();
    }

    private static int validarCargaHoraria(int cargaHoraria) {
        if (cargaHoraria <= 0) {
            throw new IllegalArgumentException(
                    "Carga horária deve ser positiva."
            );
        }

        return cargaHoraria;
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
