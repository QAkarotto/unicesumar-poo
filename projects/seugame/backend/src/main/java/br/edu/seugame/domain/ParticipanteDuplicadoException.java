package br.edu.seugame.domain;

public class ParticipanteDuplicadoException extends IllegalArgumentException {
    public ParticipanteDuplicadoException() { super("Participante duplicado."); }
}
