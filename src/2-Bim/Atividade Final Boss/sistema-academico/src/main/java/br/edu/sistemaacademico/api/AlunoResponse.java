package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.ResultadoAcademico;

import java.util.List;

public record AlunoResponse(
        String identificadorAcademico,
        String nome,
        String email,
        List<HistoricoItem> historico) {

    public record HistoricoItem(
            String codigo,
            String codigoDisciplina,
            String nomeDisciplina,
            String codigoTurma,
            String resultado) {
    }

    /** Monta histórico sem expor o grafo completo de objetos do domínio na resposta. */
    public static AlunoResponse de(Aluno aluno) {
        List<HistoricoItem> historico = aluno.getHistorico().stream()
                .map(AlunoResponse::item)
                .toList();

        return new AlunoResponse(
                aluno.getIdentificadorAcademico(),
                aluno.getNome(),
                aluno.getEmail(),
                historico);
    }

    private static HistoricoItem item(Matricula matricula) {
        ResultadoAcademico resultado = matricula.getResultado();
        return new HistoricoItem(
                matricula.getCodigo(),
                matricula.getOfertaDisciplina().getDisciplina().getCodigo(),
                matricula.getOfertaDisciplina().getDisciplina().getNome(),
                matricula.getOfertaDisciplina().getTurma().getCodigo(),
                resultado == null ? null : resultado.name());
    }
}
