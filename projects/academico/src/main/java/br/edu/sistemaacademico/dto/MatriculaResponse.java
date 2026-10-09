package br.edu.sistemaacademico.dto;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.ResultadoAcademico;
import br.edu.sistemaacademico.domain.SituacaoMatricula;
import br.edu.sistemaacademico.domain.Turma;


 //Dados da matrícula que serão devolvidos em JSON. Não é devolvido a classe Matricula direto porque ela aponta para o Aluno,
 // que aponta de volta para as Matrículas e isso faria a conversão para JSON nunca terminar.

public record MatriculaResponse(
        String codigo,
        String registroAcademico,
        String nomeAluno,
        String codigoTurma,
        String periodoLetivo,
        String codigoDisciplina,
        String nomeDisciplina,
        SituacaoMatricula situacao,
        ResultadoAcademico resultado
) {

    public static MatriculaResponse de(Matricula matricula) {
        Turma turma = matricula.getTurma();
        Disciplina disciplina = matricula.getOfertaDisciplina().getDisciplina();

        return new MatriculaResponse(
                matricula.getCodigo(),
                matricula.getAluno().getRegistroAcademico(),
                matricula.getAluno().getNome(),
                turma.getCodigo(),
                turma.getPeriodoLetivo().toString(),
                disciplina.getCodigo(),
                disciplina.getNome(),
                matricula.getSituacao(),
                matricula.getResultado()
        );
    }
}
