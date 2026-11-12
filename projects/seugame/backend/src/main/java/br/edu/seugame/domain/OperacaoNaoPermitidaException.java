package br.edu.seugame.domain;

public class OperacaoNaoPermitidaException extends IllegalStateException {
    public OperacaoNaoPermitidaException(String mensagem) { super(mensagem); }
}
