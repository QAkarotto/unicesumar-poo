package br.edu.sistemaacademico.dto;

import br.edu.sistemaacademico.domain.OfertaDisciplina;
import br.edu.sistemaacademico.domain.Semestre;

public record OfertaDisciplinaResponse(
        Long id,
        String codigo,
        String disciplinaCodigo,
        String disciplinaNome,
        int cargaHoraria,
        String turmaCodigo,
        int ano,
        Semestre semestre,
        int totalMatriculas
) {
    public static OfertaDisciplinaResponse de(OfertaDisciplina oferta) {
        var turma = oferta.getTurma();
        var disciplina = oferta.getDisciplina();
        return new OfertaDisciplinaResponse(
                oferta.getId(),
                oferta.getCodigo(),
                disciplina.getCodigo(),
                disciplina.getNome(),
                disciplina.getCargaHoraria(),
                turma.getCodigo(),
                turma.getPeriodoLetivo().getAno(),
                turma.getPeriodoLetivo().getSemestre(),
                oferta.getMatriculas() != null ? oferta.getMatriculas().size() : 0
        );
    }
}
