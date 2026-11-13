package br.edu.controleestoque.domain;

/**
 * Operação que conflita com o estado atual dos dados (ex.: excluir registro
 * referenciado, SKU/CNPJ duplicado) -> HTTP 409 (Conflict).
 */
public class OperacaoNaoPermitidaException extends RuntimeException {

    public OperacaoNaoPermitidaException(String mensagem) {
        super(mensagem);
    }
}
