package br.edu.sistemaacademico.repository;

/**
 * Lançada quando se tenta gravar um registro cuja chave já existe.
 */
public class RegistroDuplicadoException extends RuntimeException {
    public RegistroDuplicadoException(String mensagem) {
        super(mensagem);
    }
}
