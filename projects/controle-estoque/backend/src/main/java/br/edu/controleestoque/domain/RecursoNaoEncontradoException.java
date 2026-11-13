package br.edu.controleestoque.domain;

/** Recurso inexistente -> HTTP 404. */
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String recurso, Object id) {
        super(recurso + " não encontrado(a) com id " + id + ".");
    }
}
