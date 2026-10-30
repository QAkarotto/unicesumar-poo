package br.edu.sistemaacademico.repository;

/** Falha inesperada de acesso aos dados (por exemplo, um SQLException). */
public class RepositorioException extends RuntimeException {

    public RepositorioException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
