package br.edu.sistemaacademico.dto;

public record CriarOfertaRequest(
        String codigo,
        String codigoDisciplina,
        String codigoTurma
) {}
