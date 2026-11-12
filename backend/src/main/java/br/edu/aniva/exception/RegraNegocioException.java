package br.edu.aniva.exception;

// Violação de regra de negócio (ex.: e-mail já cadastrado). Vira HTTP 409.
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
