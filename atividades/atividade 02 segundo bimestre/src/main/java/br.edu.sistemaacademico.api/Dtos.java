package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.ResultadoAcademico;
import br.edu.sistemaacademico.domain.SituacaoMatricula;

import java.util.List;

/**
 * Objetos de entrada/saída da API.
 * Necessários porque o domínio tem referências circulares (Aluno -> Matricula -> Aluno):
 * serializar as entidades direto geraria recursão infinita no Jackson.
 */
public final class Dtos {

    private Dtos() {
    }

    /** JSON recebido no POST /api/matriculas. */
    public record CriarMatriculaRequest(String registroAcademico, String turma, String disciplina) {
    }

    public record MatriculaResponse(
            String codigo,
            String registroAcademico,
            String nomeAluno,
            String turma,
            String periodoLetivo,
            String disciplina,
            String nomeDisciplina,
            SituacaoMatricula situacao,
            ResultadoAcademico resultado) {

        public static MatriculaResponse de(Matricula m) {
            return new MatriculaResponse(
                    m.getCodigo(),
                    m.getAluno().getRegistroAcademico(),
                    m.getAluno().getNome(),
                    m.getTurma().getCodigo(),
                    m.getTurma().getPeriodoLetivo().toString(),
                    m.getOfertaDisciplina().getDisciplina().getCodigo(),
                    m.getOfertaDisciplina().getDisciplina().getNome(),
                    m.getSituacao(),
                    m.getResultado());
        }
    }

    public record AlunoResponse(
            String registroAcademico,
            String nome,
            String email,
            List<MatriculaResponse> matriculas) {

        public static AlunoResponse de(Aluno a) {
            return new AlunoResponse(
                    a.getRegistroAcademico(),
                    a.getNome(),
                    a.getEmail(),
                    a.getMatriculas().stream().map(MatriculaResponse::de).toList());
        }
    }
}
