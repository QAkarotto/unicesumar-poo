package br.edu.sistemaacademico.repository;

/**
 * Lançada quando se tenta cadastrar um registro cuja chave já existe
 * (RA de aluno ou código de disciplina). O Controller transforma em HTTP 409.
 */
public class RegistroDuplicadoException extends RuntimeException {

    public RegistroDuplicadoException(String mensagem) {
        super(mensagem);
    }
}
