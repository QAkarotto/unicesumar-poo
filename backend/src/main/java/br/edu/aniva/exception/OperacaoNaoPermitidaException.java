package br.edu.aniva.exception;

// A pessoa está logada, mas não pode fazer isso (ex.: apagar comentário de outra pessoa). Vira HTTP 403.
public class OperacaoNaoPermitidaException extends RuntimeException {

    public OperacaoNaoPermitidaException(String mensagem) {
        super(mensagem);
    }
}
