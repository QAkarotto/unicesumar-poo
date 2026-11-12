package br.edu.aniva.domain;

// Situação de uma obra na pauta ("para ver/ler") de uma pessoa.
public enum StatusPauta {
    A_FAZER("a-fazer"),
    ANDAMENTO("andamento"),
    CONCLUIDO("concluido");

    private final String texto;

    StatusPauta(String texto) {
        this.texto = texto;
    }

    /** Valor usado pelo frontend (ex.: "a-fazer"). */
    public String getTexto() {
        return texto;
    }

    public static StatusPauta doTexto(String texto) {
        for (StatusPauta status : values()) {
            if (status.texto.equals(texto)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Situação inválida. Use a-fazer, andamento ou concluido.");
    }
}
