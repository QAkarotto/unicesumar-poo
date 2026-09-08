package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MatriculaTest {

    @Test
    void deveConcluirMatriculaComAprovacao() {

        // Preparação
        Matricula matricula = criarMatriculaEmAndamento();

        // Ação
        matricula.concluir(ResultadoAcademico.APROVADO);

        // Verificação do resultado
        assertEquals(ResultadoAcademico.APROVADO, matricula.getResultado());
        assertEquals(SituacaoMatricula.CONCLUIDA, matricula.getSituacao());
    }

    @Test
    void devePermitirTrancarMatriculaAtiva() {

        // Preparação
        Matricula matricula = criarMatriculaEmAndamento();

        // Ação
        matricula.trancar();

        // Verificação do resultado
        assertEquals(SituacaoMatricula.TRANCADA, matricula.getSituacao());
    }

    @Test
    void devePermitirCancelarMatriculaAtiva() {

        // Preparação
        Matricula matricula = criarMatriculaEmAndamento();

        // Ação
        matricula.cancelar();

        // Verificação do resultado
        assertEquals(SituacaoMatricula.CANCELADA, matricula.getSituacao());
    }

    @Test
    void naoDevePermitirAlterarMatriculaDepoisDeConcluida() {

        // Preparação
        Matricula matricula = criarMatriculaEmAndamento();
        matricula.concluir(ResultadoAcademico.APROVADO);

        // Ação e verificação do resultado
        assertThrows(
                IllegalStateException.class,
                matricula::trancar
        );
    }

    @Test
    void devePermitirNovaMatriculaAposReprovacao() {

        // Preparação
        Aluno aluno = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola@email.com"
        );

        OfertaDisciplina oferta2025 = criarOferta(
                "ESOFT4S-NA",
                2025,
                Semestre.SEGUNDO
        );

        OfertaDisciplina oferta2026 = criarOferta(
                "ESOFT4S-NB",
                2026,
                Semestre.PRIMEIRO
        );

        Matricula primeiraMatricula = oferta2025.matricular(
                "MAT-001",
                aluno
        );

        primeiraMatricula.concluir(ResultadoAcademico.REPROVADO);

        // Ação
        Matricula novaMatricula = oferta2026.matricular(
                "MAT-002",
                aluno
        );

        // Verificação do resultado
        assertEquals(2, aluno.getMatriculas().size());
        assertEquals(SituacaoMatricula.ATIVA, novaMatricula.getSituacao());
    }

    @Test
    void naoDevePermitirNovaMatriculaAposAprovacao() {

        // Preparação
        Aluno aluno = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola@email.com"
        );

        OfertaDisciplina oferta2025 = criarOferta(
                "ESOFT4S-NA",
                2025,
                Semestre.SEGUNDO
        );

        OfertaDisciplina oferta2026 = criarOferta(
                "ESOFT4S-NB",
                2026,
                Semestre.PRIMEIRO
        );

        Matricula primeiraMatricula = oferta2025.matricular(
                "MAT-001",
                aluno
        );

        primeiraMatricula.concluir(ResultadoAcademico.APROVADO);

        // Ação e verificação do resultado
        assertThrows(
                IllegalStateException.class,
                () -> oferta2026.matricular("MAT-002", aluno)
        );
    }

    private Matricula criarMatriculaEmAndamento() {

        Aluno aluno = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola@email.com"
        );

        OfertaDisciplina oferta = criarOferta(
                "ESOFT4S-NA",
                2026,
                Semestre.PRIMEIRO
        );

        return oferta.matricular("MAT-001", aluno);
    }

    private OfertaDisciplina criarOferta(
            String codigoTurma,
            int ano,
            Semestre semestre
    ) {

        Turma turma = new Turma(
                codigoTurma,
                new PeriodoLetivo(ano, semestre)
        );

        Disciplina poo = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        return turma.ofertarDisciplina(poo);
    }
}