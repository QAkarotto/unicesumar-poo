package br.edu.agroassist.domain;

import br.edu.agroassist.exception.DadosInvalidosException;

final class Validacao {
    private Validacao() {}

    static String obrigatorio(String valor, int limite, String campo) {
        if (valor == null || valor.isBlank()) throw new DadosInvalidosException("Informe " + campo + ".");
        return texto(valor, limite, campo);
    }

    static String texto(String valor, int limite, String campo) {
        String texto = valor == null ? "" : valor.trim();
        if (texto.length() > limite)
            throw new DadosInvalidosException(campo + ": limite de " + limite + " caracteres.");
        return texto;
    }
}
