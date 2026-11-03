package br.edu.sistemaacademico.repository;

public class ErroPersistenciaException extends RuntimeException {

    public ErroPersistenciaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}