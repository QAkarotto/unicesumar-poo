package br.edu.sistemaacademico.api.infra;

public class ErroDeAcessoADadosException extends RuntimeException {
    public ErroDeAcessoADadosException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}