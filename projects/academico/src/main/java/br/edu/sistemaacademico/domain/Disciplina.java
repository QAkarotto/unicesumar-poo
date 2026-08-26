package br.edu.sistemaacademico.domain;

import java.util.Objects;

public class Disciplina {

    private final String codigo;   // identidade da disciplina: não muda
    private String nome;           // pode ser corrigido
    private int cargaHoraria;      // pode mudar em revisão da matriz curricular

    public Disciplina(String codigo, String nome, int cargaHoraria) {
        this.codigo = validarTexto(codigo, "Código da disciplina");
        this.nome = validarTexto(nome, "Nome da disciplina");
        this.cargaHoraria = validarCargaHoraria(cargaHoraria);
    }

    // ---------- Métodos de acesso ----------

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    // ---------- Métodos de alteração ----------

    public void setNome(String nome) {
        this.nome = validarTexto(nome, "Nome da disciplina");
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = validarCargaHoraria(cargaHoraria);
    }

    // ---------- Regras privadas ----------

    private static String validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " é obrigatório e não pode ser vazio.");
        }
        return valor.trim();
    }

    private static int validarCargaHoraria(int cargaHoraria) {
        if (cargaHoraria <= 0) {
            throw new IllegalArgumentException("Carga horária deve ser positiva. Valor recebido: " + cargaHoraria);
        }
        return cargaHoraria;
    }

    // ---------- Identidade ----------

    /*
     * Duas disciplinas são a mesma quando têm o mesmo código.
     * É isso que sustenta a regra "não pode cursar de novo o que já foi aprovado",
     * mesmo que a oferta seja de outra turma ou período.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Disciplina outra)) {
            return false;
        }
        return codigo.equalsIgnoreCase(outra.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo.toLowerCase());
    }

    @Override
    public String toString() {
        return "Disciplina [" + codigo + "] " + nome + " (" + cargaHoraria + "h)";
    }
}