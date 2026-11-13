package br.edu.controleestoque.domain;

/** Saída maior que o saldo disponível do produto. */
public class EstoqueInsuficienteException extends RegraNegocioException {

    public EstoqueInsuficienteException(String sku, int saldo, int solicitado) {
        super("Estoque insuficiente para o produto " + sku
                + ": saldo atual " + saldo + ", solicitado " + solicitado + ".");
    }
}
