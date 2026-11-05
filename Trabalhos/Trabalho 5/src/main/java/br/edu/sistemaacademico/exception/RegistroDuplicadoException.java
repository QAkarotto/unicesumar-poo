package br.edu.sistemaacademico.exception;

public class RegistroDuplicadoException extends RuntimeException {

    public RegistroDuplicadoException(String mensagem) {
        super(mensagem);
    }
}
