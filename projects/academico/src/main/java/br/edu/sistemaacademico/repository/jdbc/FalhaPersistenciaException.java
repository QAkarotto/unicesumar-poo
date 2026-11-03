package br.edu.sistemaacademico.repository.jdbc;

/** Indica uma falha de acesso ao banco que não representa conflito do cliente. */
public class FalhaPersistenciaException extends RuntimeException {
    public FalhaPersistenciaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
