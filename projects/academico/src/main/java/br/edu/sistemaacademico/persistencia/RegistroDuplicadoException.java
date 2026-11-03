package br.edu.sistemaacademico.persistencia;

public class RegistroDuplicadoException extends RuntimeException {
    public RegistroDuplicadoException(String mensagem) {
        super(mensagem);
    }
}
