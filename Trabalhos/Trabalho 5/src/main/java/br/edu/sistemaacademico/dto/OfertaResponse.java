package br.edu.sistemaacademico.dto;

import br.edu.sistemaacademico.domain.OfertaDisciplina;

public record OfertaResponse(Long id, String turma, int ano, String semestre, String disciplina, String nomeDisciplina) {

    public static OfertaResponse de(OfertaDisciplina oferta) {
        return new OfertaResponse(
                oferta.getId(),
                oferta.getTurma().getCodigo(),
                oferta.getTurma().getPeriodoLetivo().getAno(),
                oferta.getTurma().getPeriodoLetivo().getSemestre().name(),
                oferta.getDisciplina().getCodigo(),
                oferta.getDisciplina().getNome()
        );
    }
}
