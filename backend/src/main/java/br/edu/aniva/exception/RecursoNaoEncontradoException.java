package br.edu.aniva.exception;

// Recurso inexistente (obra, comentário...). Vira HTTP 404.
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
