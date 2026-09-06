package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Turma")
class TurmaTest {

    private PeriodoLetivo periodo2026_2;
    private Disciplina poo;
    private Disciplina bancoDados;

    @BeforeEach
    void preparar() {
        periodo2026_2 = new PeriodoLetivo(2026, Semestre.SEGUNDO);
        poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        bancoDados = new Disciplina("BD", "Banco de Dados", 80);
    }

    @Test
    @DisplayName("Deve criar uma turma sem nenhuma disciplina ofertada")
    void deveCriarTurmaSemOfertas() {
        // Act
        var turma = new Turma("  ESOFT4S-NA  ", periodo2026_2);

        // Assert
        assertEquals("ESOFT4S-NA", turma.getCodigo());
        assertEquals(periodo2026_2, turma.getPeriodoLetivo());
        assertTrue(turma.getOfertas().isEmpty());
    }

    @Test
    @DisplayName("Deve recusar código ou período letivo inválidos")
    void deveRecusarDadosObrigatorios() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Turma(null, periodo2026_2)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Turma("   ", periodo2026_2)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Turma("ESOFT4S-NA", null)
        );
    }

    @Test
    @DisplayName("Deve ofertar várias disciplinas diferentes no mesmo período letivo")
    void deveOfertarVariasDisciplinas() {
        // Arrange
        var turma = new Turma("ESOFT4S-NA", periodo2026_2);

        // Act
        var ofertaPoo = turma.ofertarDisciplina(poo);
        var ofertaBd = turma.ofertarDisciplina(bancoDados);

        // Assert
        assertEquals(2, turma.getOfertas().size());
        assertSame(turma, ofertaPoo.getTurma());
        assertEquals(poo, ofertaPoo.getDisciplina());
        assertEquals(bancoDados, ofertaBd.getDisciplina());
        assertTrue(turma.getOfertas().contains(ofertaPoo));
        assertTrue(turma.getOfertas().contains(ofertaBd));
    }

    @Test
    @DisplayName("Deve impedir a oferta duplicada da mesma disciplina na turma")
    void deveImpedirOfertaDuplicada() {
        // Arrange
        var turma = new Turma("ESOFT4S-NA", periodo2026_2);
        turma.ofertarDisciplina(poo);

        // Act
        var erro = assertThrows(
                IllegalArgumentException.class,
                () -> turma.ofertarDisciplina(new Disciplina("POO", "Outro nome", 40))
        );

        // Assert
        assertEquals("A disciplina já foi ofertada para esta turma.", erro.getMessage());
        assertEquals(1, turma.getOfertas().size());
    }

    @Test
    @DisplayName("Deve recusar a oferta de uma disciplina nula")
    void deveRecusarDisciplinaNula() {
        var turma = new Turma("ESOFT4S-NA", periodo2026_2);

        assertThrows(
                IllegalArgumentException.class,
                () -> turma.ofertarDisciplina(null)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Turma("ESOFT4S-NB", null, periodo2026_2)
        );
    }

    @Test
    @DisplayName("Deve criar a turma já com uma disciplina ofertada")
    void deveCriarTurmaComDisciplina() {
        // Act
        var turma = new Turma("ADSIS4S", poo, periodo2026_2);

        // Assert
        assertEquals(1, turma.getOfertas().size());
        assertEquals(poo, turma.getOfertas().get(0).getDisciplina());
    }

    @Test
    @DisplayName("Deve expor as ofertas como coleção imutável")
    void deveExporOfertasImutaveis() {
        // Arrange
        var turma = new Turma("ESOFT4S-NA", periodo2026_2);
        var oferta = turma.ofertarDisciplina(poo);

        // Act
        var ofertas = turma.getOfertas();

        // Assert
        assertThrows(
                UnsupportedOperationException.class,
                () -> ofertas.add(oferta)
        );
    }

    @Test
    @DisplayName("Deve exigir uma única oferta para matricular a partir da turma")
    void deveExigirOfertaUnicaParaMatricularPelaTurma() {
        // Arrange
        var aluno = new Aluno("RA2026001", "Arthur Pacher", "arthur@email.com");
        var turmaSemOferta = new Turma("ESOFT4S-NA", periodo2026_2);
        var turmaComDuasOfertas = new Turma("ESOFT4S-NB", periodo2026_2);
        turmaComDuasOfertas.ofertarDisciplina(poo);
        turmaComDuasOfertas.ofertarDisciplina(bancoDados);

        // Act / Assert
        assertThrows(
                IllegalStateException.class,
                () -> new Matricula("MAT-1", aluno, turmaSemOferta)
        );
        assertThrows(
                IllegalStateException.class,
                () -> new Matricula("MAT-2", aluno, turmaComDuasOfertas)
        );
    }

    @Test
    @DisplayName("Deve apresentar código e período letivo no toString")
    void deveApresentarRepresentacaoTextual() {
        var turma = new Turma("ESOFT4S-NA", periodo2026_2);

        assertEquals("ESOFT4S-NA - 2026/2", turma.toString());
    }
}
