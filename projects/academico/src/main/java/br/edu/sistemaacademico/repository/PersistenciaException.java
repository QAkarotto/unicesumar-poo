package br.edu.sistemaacademico.repository;

/** Falha inesperada de acesso ao banco (encapsula SQLException). */
public class PersistenciaException extends RuntimeException {
    public PersistenciaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
