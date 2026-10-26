package br.edu.sistemaacademico.repository;

public class RepositorioException extends RuntimeException {

    public RepositorioException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
