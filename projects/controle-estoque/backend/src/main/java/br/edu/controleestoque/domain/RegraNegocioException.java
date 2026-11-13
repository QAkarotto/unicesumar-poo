package br.edu.controleestoque.domain;

/**
 * Violação de uma regra de negócio do domínio -> HTTP 422 (Unprocessable Content).
 * Os dados estão bem formados, mas a operação não faz sentido no estado atual.
 */
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
