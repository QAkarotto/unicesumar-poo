package br.edu.sistemaacademico.repository;

/** Falha inesperada de acesso ao banco (encapsula SQLException; a API traduz para 500). */
public class PersistenciaException extends RuntimeException {
    public PersistenciaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
