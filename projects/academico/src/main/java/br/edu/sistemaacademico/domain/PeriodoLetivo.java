package br.edu.sistemaacademico.domain;

/*
 * Classe totalmente IMUTÁVEL: 2025/1 é 2025/1 para sempre.
 * Se precisar de outro período, cria-se um novo objeto.
 */
public class PeriodoLetivo {

    private final int ano;
    private final Semestre semestre;

    public PeriodoLetivo(int ano, Semestre semestre) {
        if (ano <= 0) {
            throw new IllegalArgumentException("Ano do período letivo deve ser positivo. Valor recebido: " + ano);
        }
        if (semestre == null) {
            throw new IllegalArgumentException("Semestre é obrigatório.");
        }
        this.ano = ano;
        this.semestre = semestre;
    }

    public int getAno() {
        return ano;
    }

    public Semestre getSemestre() {
        return semestre;
    }

    // Sem setters: nada aqui pode mudar depois de criado.

    @Override
    public String toString() {
        return ano + " - " + semestre.getDescricao();
    }
}