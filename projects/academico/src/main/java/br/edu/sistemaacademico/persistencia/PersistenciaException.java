package br.edu.sistemaacademico.persistencia;

// falha no acesso ao banco que nao e culpa de quem fez a requisicao (arquivo travado, tabela ausente...)
public class PersistenciaException extends RuntimeException {
    public PersistenciaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
