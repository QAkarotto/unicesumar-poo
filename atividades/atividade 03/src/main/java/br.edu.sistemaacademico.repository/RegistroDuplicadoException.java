package br.edu.sistemaacademico.repository;

/** Tentativa de gravar um registro cuja chave já existe (a API traduz para 409 Conflict). */
public class RegistroDuplicadoException extends RuntimeException {
    public RegistroDuplicadoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
