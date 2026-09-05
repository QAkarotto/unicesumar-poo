package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TurmaTest {

    private PeriodoLetivo periodo2026_1() {
        return new PeriodoLetivo(2026, Semestre.PRIMEIRO);
    }

    @Test
    void deveCriarTurmaComDadosValidos() {
        // Arrange
        PeriodoLetivo periodo = periodo2026_1();

        // Act
        Turma turma = new Turma("T01", periodo);

        // Assert
        assertEquals("T01", turma.getCodigo());
        assertEquals(periodo, turma.getPeriodoLetivo());
        assertTrue(turma.getOfertas().isEmpty());
    }

    @Test
    void deveCriarTurmaComDisciplinaViaConstrutorConveniente() {
        // Arrange
        Disciplina disciplina = new Disciplina("POO001", "Programação", 80);

        // Act
        Turma turma = new Turma("T01", disciplina, periodo2026_1());

        // Assert
        assertEquals(1, turma.getOfertas().size());
        assertEquals(disciplina, turma.getOfertas().getFirst().getDisciplina());
    }

    @Test
    void naoDeveCriarTurmaSemCodigo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Turma("", periodo2026_1()));
    }

    @Test
    void naoDeveCriarTurmaComCodigoNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Turma(null, periodo2026_1()));
    }

    @Test
    void naoDeveCriarTurmaSemPeriodoLetivo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Turma("T01", null));
    }

    @Test
    void deveOfertarDisciplinaParaTurma() {
        // Arrange
        Turma turma = new Turma("T01", periodo2026_1());
        Disciplina disciplina = new Disciplina("POO001", "Programação", 80);

        // Act
        OfertaDisciplina oferta = turma.ofertarDisciplina(disciplina);

        // Assert
        assertEquals(1, turma.getOfertas().size());
        assertEquals(disciplina, oferta.getDisciplina());
        assertEquals(turma, oferta.getTurma());
    }

    @Test
    void deveOfertarMultiplasDisciplinas() {
        // Arrange
        Turma turma = new Turma("T01", periodo2026_1());
        Disciplina poo = new Disciplina("POO001", "Programação", 80);
        Disciplina bd  = new Disciplina("BD001", "Banco de Dados", 60);

        // Act
        turma.ofertarDisciplina(poo);
        turma.ofertarDisciplina(bd);

        // Assert
        assertEquals(2, turma.getOfertas().size());
    }

    @Test
    void naoDeveOfertarDisciplinaNula() {
        // Arrange
        Turma turma = new Turma("T01", periodo2026_1());

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> turma.ofertarDisciplina(null));
    }

    @Test
    void naoDeveOfertarMesmaDisciplinaDuasVezes() {
        // Arrange
        Disciplina disciplina = new Disciplina("POO001", "Programação", 80);
        Turma turma = new Turma("T01", disciplina, periodo2026_1());

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> turma.ofertarDisciplina(disciplina));
    }

    @Test
    void deveRetornarUnicaOfertaQuandoHaSoUma() {
        // Arrange
        Disciplina disciplina = new Disciplina("POO001", "Programação", 80);
        Turma turma = new Turma("T01", disciplina, periodo2026_1());

        // Act – via Matricula (que chama obterUnicaOferta internamente)
        Aluno aluno = new Aluno("RA001", "Gustavo", "g@email.com");
        Matricula matricula = new Matricula("MAT001", aluno, turma);

        // Assert
        assertNotNull(matricula);
        assertEquals(turma, matricula.getTurma());
    }

    @Test
    void naoDeveRetornarUnicaOfertaQuandoTurmaVazia() {
        // Arrange
        Turma turma = new Turma("T01", periodo2026_1());
        Aluno aluno = new Aluno("RA001", "Gustavo", "g@email.com");

        // Act & Assert – Matricula via turma sem oferta deve lançar erro
        assertThrows(IllegalStateException.class,
                () -> new Matricula("MAT001", aluno, turma));
    }

    @Test
    void naoDeveRetornarUnicaOfertaQuandoHaMultiplas() {
        // Arrange
        Turma turma = new Turma("T01", periodo2026_1());
        turma.ofertarDisciplina(new Disciplina("POO001", "Programação", 80));
        turma.ofertarDisciplina(new Disciplina("BD001", "Banco de Dados", 60));
        Aluno aluno = new Aluno("RA001", "Gustavo", "g@email.com");

        // Act & Assert – Matricula via turma com múltiplas ofertas deve lançar erro
        assertThrows(IllegalStateException.class,
                () -> new Matricula("MAT001", aluno, turma));
    }

    @Test
    void deveRetornarToStringComCodigoEPeriodo() {
        // Arrange
        Turma turma = new Turma("T01", periodo2026_1());

        // Act
        String resultado = turma.toString();

        // Assert
        assertTrue(resultado.contains("T01"));
        assertTrue(resultado.contains("2026"));
    }
}
