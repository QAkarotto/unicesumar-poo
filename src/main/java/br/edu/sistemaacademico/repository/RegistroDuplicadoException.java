package br.edu.sistemaacademico.repository;

public class RegistroDuplicadoException extends PersistenciaException {

    public RegistroDuplicadoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
