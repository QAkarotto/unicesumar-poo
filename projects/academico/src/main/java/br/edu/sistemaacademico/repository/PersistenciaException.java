package br.edu.sistemaacademico.repository;

/**
 * Falha inesperada ao acessar o mecanismo de persistência.
 * <p>
 * Envolve a exceção original (por exemplo, uma {@code SQLException}) para que
 * as camadas de cima não dependam de classes de JDBC.
 */
public class PersistenciaException extends RuntimeException {
    public PersistenciaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
