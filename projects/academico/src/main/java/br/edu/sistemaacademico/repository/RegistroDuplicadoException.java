package br.edu.sistemaacademico.repository;

/** Tentativa de gravar um registro cuja chave já existe. */
public class RegistroDuplicadoException extends RepositorioException {

    public RegistroDuplicadoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
