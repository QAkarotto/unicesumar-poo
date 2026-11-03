package br.edu.sistemaacademico.repository;

/**
 * Erro inesperado ao acessar o banco de dados.
 *
 * Envolve a SQLException original (que fica em getCause()), assim as camadas
 * de cima não precisam conhecer classes de JDBC.
 */
public class PersistenciaException extends RuntimeException {

    public PersistenciaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
