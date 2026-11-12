package br.edu.seugame.domain;

final class DadosDominio {
    private DadosDominio() {}

    static String aparar(String valor) {
        return valor == null ? "" : valor.replaceAll("(?U)^\\s+|\\s+$", "");
    }

    static String texto(String valor, int minimo, int maximo, String campo) {
        String normalizado = aparar(valor);
        int tamanho = normalizado.codePointCount(0, normalizado.length());
        if (tamanho < minimo || tamanho > maximo) {
            throw new IllegalArgumentException(campo + " deve ter entre " + minimo + " e " + maximo + " caracteres.");
        }
        return normalizado;
    }
}
