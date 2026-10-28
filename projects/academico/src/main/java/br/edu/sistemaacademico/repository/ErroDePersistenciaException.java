package br.edu.sistemaacademico.repository;

public class ErroDePersistenciaException extends RuntimeException {

    public ErroDePersistenciaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
