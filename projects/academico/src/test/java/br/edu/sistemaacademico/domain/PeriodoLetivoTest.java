package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PeriodoLetivoTest {

    @Test
    @DisplayName("Ano precisa ser positivo e o semestre é obrigatório")
    void deveValidarAnoESemestre() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(0, Semestre.PRIMEIRO)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(-2026, Semestre.PRIMEIRO)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new PeriodoLetivo(2026, null)
        );
    }

    @Test
    @DisplayName("Período letivo é identificado pelo ano e pelo semestre")
    void periodoEIdentificadoPorAnoESemestre() {
        // Arrange
        var periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);
        var mesmoPeriodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);
        var outroSemestre = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        var outroAno = new PeriodoLetivo(2025, Semestre.SEGUNDO);

        // Assert
        assertTrue(periodo.equals(mesmoPeriodo));
        assertEquals(periodo.hashCode(), mesmoPeriodo.hashCode());
        assertFalse(periodo.equals(outroSemestre));
        assertFalse(periodo.equals(outroAno));
        assertFalse(periodo.equals(2026));
    }

    @Test
    @DisplayName("Período letivo é escrito no formato ano/semestre")
    void toStringUsaFormatoAnoBarraSemestre() {
        assertEquals("2026/1", new PeriodoLetivo(2026, Semestre.PRIMEIRO).toString());
        assertEquals("2026/2", new PeriodoLetivo(2026, Semestre.SEGUNDO).toString());
        assertEquals(2, Semestre.SEGUNDO.getNumero());
    }

    @Test
    @DisplayName("Turmas do mesmo código em períodos diferentes são ofertas separadas")
    void turmasDePeriodosDiferentesNaoSeMisturam() {
        var disciplina = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var aluno = new Aluno("RA300", "Eduarda Lima", "eduarda@email.com");

        var turmaDe2025 = new Turma("ADSIS4S-NA", disciplina, new PeriodoLetivo(2025, Semestre.SEGUNDO));
        var turmaDe2026 = new Turma("ADSIS4S-NA", disciplina, new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        turmaDe2025.getOfertas().get(0).matricular(aluno).concluir(ResultadoAcademico.REPROVADO);
        turmaDe2026.getOfertas().get(0).matricular(aluno);

        assertEquals(2, aluno.getMatriculas().size());
    }
}
