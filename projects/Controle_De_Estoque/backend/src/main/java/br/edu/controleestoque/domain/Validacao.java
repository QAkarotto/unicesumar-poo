package br.edu.controleestoque.domain;

/**
 * Validacoes simples compartilhadas pelas entidades do dominio.
 */
final class Validacao {

    private Validacao() {
    }

    static String textoObrigatorio(String valor, String mensagem) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensagem);
        }
        return valor.trim();
    }
}
