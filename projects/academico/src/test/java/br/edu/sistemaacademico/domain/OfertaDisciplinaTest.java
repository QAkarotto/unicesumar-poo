package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OfertaDisciplinaTest {

    @Test
    void deveMatricularAlunoERegistrarNoHistorico() {

        // Preparação
        Aluno aluno = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola@email.com"
        );

        Disciplina poo = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        Turma turma = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2026, Semestre.PRIMEIRO)
        );

        OfertaDisciplina oferta = turma.ofertarDisciplina(poo);

        // Ação
        Matricula matricula = oferta.matricular(aluno);

        // Verificação do resultado
        assertEquals(aluno, matricula.getAluno());
        assertEquals(1, oferta.getMatriculas().size());
        assertEquals(1, aluno.getMatriculas().size());
    }

    @Test
    void naoDevePermitirDuasMatriculasDoMesmoAlunoNaMesmaOferta() {

        // Preparação
        Aluno aluno = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola@email.com"
        );

        Disciplina poo = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        Turma turma = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2026, Semestre.PRIMEIRO)
        );

        OfertaDisciplina oferta = turma.ofertarDisciplina(poo);
        oferta.matricular(aluno);

        // Ação e verificação do resultado
        assertThrows(
                IllegalStateException.class,
                () -> oferta.matricular(aluno)
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

        Disciplina poo = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        Turma turma2025 = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2025, Semestre.SEGUNDO)
        );

        Turma turma2026 = new Turma(
                "ESOFT4S-NB",
                new PeriodoLetivo(2026, Semestre.PRIMEIRO)
        );

        OfertaDisciplina oferta2025 = turma2025.ofertarDisciplina(poo);
        OfertaDisciplina oferta2026 = turma2026.ofertarDisciplina(poo);

        Matricula primeiraMatricula = oferta2025.matricular(aluno);
        primeiraMatricula.concluir(ResultadoAcademico.REPROVADO);

        // Ação
        Matricula novaMatricula = oferta2026.matricular(aluno);

        // Verificação do resultado
        assertEquals(2, aluno.getMatriculas().size());
        assertEquals(aluno, novaMatricula.getAluno());
    }

    @Test
    void naoDevePermitirNovaMatriculaAposAprovacao() {

        // Preparação
        Aluno aluno = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola@email.com"
        );

        Disciplina poo = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        Turma turma2025 = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2025, Semestre.SEGUNDO)
        );

        Turma turma2026 = new Turma(
                "ESOFT4S-NB",
                new PeriodoLetivo(2026, Semestre.PRIMEIRO)
        );

        OfertaDisciplina oferta2025 = turma2025.ofertarDisciplina(poo);
        OfertaDisciplina oferta2026 = turma2026.ofertarDisciplina(poo);

        Matricula primeiraMatricula = oferta2025.matricular(aluno);
        primeiraMatricula.concluir(ResultadoAcademico.APROVADO);

        // Ação e verificação do resultado
        assertThrows(
                IllegalStateException.class,
                () -> oferta2026.matricular(aluno)
        );
    }
}