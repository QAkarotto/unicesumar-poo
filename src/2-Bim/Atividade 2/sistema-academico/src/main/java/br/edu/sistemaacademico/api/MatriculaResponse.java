package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import br.edu.sistemaacademico.domain.ResultadoAcademico;
import br.edu.sistemaacademico.domain.Turma;

public record MatriculaResponse(
        String codigo,
        AlunoResumo aluno,
        DisciplinaResumo disciplina,
        String codigoTurma,
        int ano,
        String semestre,
        String resultado) {

    public record AlunoResumo(String identificadorAcademico, String nome, String email) {
    }

    public record DisciplinaResumo(String codigo, String nome, int cargaHoraria) {
    }

    /** Projeção plana: evita ciclo Aluno ↔ Matricula na serialização JSON (Jackson). */
    public static MatriculaResponse de(Matricula matricula) {
        Aluno aluno = matricula.getAluno();
        OfertaDisciplina oferta = matricula.getOfertaDisciplina();
        Disciplina disciplina = oferta.getDisciplina();
        Turma turma = oferta.getTurma();
        ResultadoAcademico resultado = matricula.getResultado();

        return new MatriculaResponse(
                matricula.getCodigo(),
                new AlunoResumo(aluno.getIdentificadorAcademico(), aluno.getNome(), aluno.getEmail()),
                new DisciplinaResumo(disciplina.getCodigo(), disciplina.getNome(), disciplina.getCargaHoraria()),
                turma.getCodigo(),
                turma.getPeriodoLetivo().getAno(),
                turma.getPeriodoLetivo().getSemestre().name(),
                resultado == null ? null : resultado.name());
    }
}
