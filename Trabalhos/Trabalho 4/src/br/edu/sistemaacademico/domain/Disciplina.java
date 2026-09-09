package br.edu.sistemaacademico.domain;

public class Disciplina {
    private String sigla;
    private String nome;
    private int cargaHoraria;

    // O construtor agora aceita os TRÊS parâmetros que o professor enviou no SistemaAcademico
    public Disciplina(String sigla, String nome, int cargaHoraria) {
        this.sigla = sigla;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
    }

    public String getSigla() {
        return sigla;
    }

    public String getNome() {
        return nome;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    @Override
    public String toString() {
        return nome; // Retorna o nome por extenso para ficar bonito no console
    }
}