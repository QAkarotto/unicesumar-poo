package br.edu.sistemaacademico.repository;

public class RegistroDuplicadoException extends RuntimeException {
    public RegistroDuplicadoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
