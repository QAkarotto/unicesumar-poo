package br.edu.sistemaacademico.repository;

public class ErroBancoDadosException extends RuntimeException {
    public ErroBancoDadosException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
