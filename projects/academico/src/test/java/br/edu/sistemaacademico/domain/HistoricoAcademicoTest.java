package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HistoricoAcademicoTest {

    @Test
    @DisplayName("Deve permitir nova matrícula na disciplina após reprovação")
    void devePermitirNovaMatriculaAposReprovacao() {
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");
        var poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);

        var turmaAnterior = new Turma(
                "ESOFT3S-NA",
                new PeriodoLetivo(2026, Semestre.PRIMEIRO)
        );
        var ofertaAnterior = turmaAnterior.ofertarDisciplina(poo);
        var matriculaAnterior = ofertaAnterior.matricular(aluno);
        matriculaAnterior.concluir(ResultadoAcademico.REPROVADO);

        var novaTurma = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2026, Semestre.SEGUNDO)
        );
        var novaOferta = novaTurma.ofertarDisciplina(poo);

        var novaMatricula = novaOferta.matricular(aluno);

        assertEquals(2, aluno.getMatriculas().size());
        assertSame(novaMatricula, novaOferta.getMatriculas().get(0));
        assertNull(novaMatricula.getResultado());
    }

    @Test
    @DisplayName("Deve impedir nova matrícula na disciplina após aprovação")
    void deveImpedirNovaMatriculaAposAprovacao() {
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");
        var poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);

        var turmaAnterior = new Turma(
                "ESOFT3S-NA",
                new PeriodoLetivo(2026, Semestre.PRIMEIRO)
        );
        var ofertaAnterior = turmaAnterior.ofertarDisciplina(poo);
        var matriculaAnterior = ofertaAnterior.matricular(aluno);
        matriculaAnterior.concluir(ResultadoAcademico.APROVADO);

        var novaTurma = new Turma(
                "ESOFT4S-NB",
                new PeriodoLetivo(2026, Semestre.SEGUNDO)
        );
        var novaOferta = novaTurma.ofertarDisciplina(poo);

        assertThrows(
                IllegalStateException.class,
                () -> novaOferta.matricular(aluno)
        );
        assertEquals(1, aluno.getMatriculas().size());
        assertEquals(0, novaOferta.getMatriculas().size());
    }
}
