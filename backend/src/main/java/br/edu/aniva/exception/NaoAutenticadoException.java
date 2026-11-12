package br.edu.aniva.exception;

// Pessoa sem sessão ativa ou com credenciais erradas. Vira HTTP 401.
public class NaoAutenticadoException extends RuntimeException {

    public NaoAutenticadoException(String mensagem) {
        super(mensagem);
    }
}
