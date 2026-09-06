package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Historico academico do aluno")
class HistoricoAcademicoTest {

    private Aluno paola;
    private Disciplina poo;
    private Disciplina bancoDados;
    private OfertaDisciplina poo2025;
    private OfertaDisciplina poo2026;
    private OfertaDisciplina bancoDados2026;

    @BeforeEach
    void preparar() {
        paola = new Aluno("RA2026001", "Paola Oliveira", "paola.oliveira@email.com");
        poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        bancoDados = new Disciplina("BD", "Banco de Dados", 80);

        var turma2025 = new Turma("ESOFT4S-NA", new PeriodoLetivo(2025, Semestre.SEGUNDO));
        var turma2026 = new Turma("ESOFT4S-NB", new PeriodoLetivo(2026, Semestre.PRIMEIRO));

        poo2025 = turma2025.ofertarDisciplina(poo);
        poo2026 = turma2026.ofertarDisciplina(poo);
        bancoDados2026 = turma2026.ofertarDisciplina(bancoDados);
    }

    @Test
    @DisplayName("Deve permitir nova matrícula na mesma disciplina após reprovação")
    void devePermitirNovaMatriculaAposReprovacao() {
        // Arrange
        var primeiraTentativa = poo2025.matricular("MAT-2025", paola);
        primeiraTentativa.concluir(ResultadoAcademico.REPROVADO);

        // Act
        var segundaTentativa = poo2026.matricular("MAT-2026", paola);

        // Assert
        assertNotSame(primeiraTentativa, segundaTentativa);
        assertEquals(SituacaoMatricula.ATIVA, segundaTentativa.getSituacao());
        assertEquals(2, paola.getMatriculas().size());
    }

    @Test
    @DisplayName("Deve impedir nova matrícula na mesma disciplina após aprovação, mesmo em outra turma")
    void deveImpedirNovaMatriculaAposAprovacao() {
        // Arrange
        var matricula2025 = poo2025.matricular("MAT-2025", paola);
        matricula2025.concluir(ResultadoAcademico.APROVADO);

        // Act
        var erro = assertThrows(
                IllegalStateException.class,
                () -> poo2026.matricular("MAT-2026", paola)
        );

        // Assert
        assertEquals("O aluno já foi aprovado nesta disciplina.", erro.getMessage());
        assertEquals(1, paola.getMatriculas().size());
        assertTrue(poo2026.getMatriculas().isEmpty());
    }

    @Test
    @DisplayName("Aprovação em uma disciplina não deve bloquear matrícula em outra disciplina")
    void aprovacaoNaoDeveBloquearOutraDisciplina() {
        // Arrange
        var matriculaPoo = poo2025.matricular("MAT-POO", paola);
        matriculaPoo.concluir(ResultadoAcademico.APROVADO);

        // Act
        var matriculaBd = bancoDados2026.matricular("MAT-BD", paola);

        // Assert
        assertEquals(bancoDados, matriculaBd.getOfertaDisciplina().getDisciplina());
        assertEquals(2, paola.getMatriculas().size());
    }

    @Test
    @DisplayName("Matrícula trancada não conclui a disciplina e permite cursar novamente")
    void matriculaTrancadaPermiteCursarNovamente() {
        // Arrange
        var matricula2025 = poo2025.matricular("MAT-2025", paola);
        matricula2025.trancar();

        // Act
        var matricula2026 = poo2026.matricular("MAT-2026", paola);

        // Assert
        assertEquals(SituacaoMatricula.TRANCADA, matricula2025.getSituacao());
        assertEquals(SituacaoMatricula.ATIVA, matricula2026.getSituacao());
        assertEquals(2, paola.getMatriculas().size());
    }

    @Test
    @DisplayName("Deve manter o histórico consistente quando uma matrícula é recusada")
    void deveManterHistoricoConsistenteAposRecusa() {
        // Arrange
        var matricula = poo2025.matricular("MAT-2025", paola);

        // Act
        assertThrows(
                IllegalArgumentException.class,
                () -> poo2025.matricular("MAT-DUPLICADA", paola)
        );

        // Assert
        assertEquals(1, paola.getMatriculas().size());
        assertEquals(1, poo2025.getMatriculas().size());
        assertTrue(paola.getMatriculas().contains(matricula));
        assertFalse(
                paola.getMatriculas().stream()
                        .anyMatch(item -> item.getCodigo().equals("MAT-DUPLICADA"))
        );
    }

    @Test
    @DisplayName("Deve acumular no histórico as matrículas de todas as ofertas cursadas")
    void deveAcumularMatriculasNoHistorico() {
        // Arrange
        var reprovacao = poo2025.matricular("MAT-POO-2025", paola);
        reprovacao.concluir(ResultadoAcademico.REPROVADO);
        var aprovacao = poo2026.matricular("MAT-POO-2026", paola);
        aprovacao.concluir(ResultadoAcademico.APROVADO);
        var bancoDadosMatricula = bancoDados2026.matricular("MAT-BD-2026", paola);

        // Act
        var historico = paola.getMatriculas();

        // Assert
        assertEquals(3, historico.size());
        assertEquals(reprovacao, historico.get(0));
        assertEquals(aprovacao, historico.get(1));
        assertEquals(bancoDadosMatricula, historico.get(2));
        assertEquals(ResultadoAcademico.REPROVADO, historico.get(0).getResultado());
        assertEquals(ResultadoAcademico.APROVADO, historico.get(1).getResultado());
        assertEquals(SituacaoMatricula.ATIVA, historico.get(2).getSituacao());
    }

    @Test
    @DisplayName("Deve isolar o histórico entre alunos diferentes")
    void deveIsolarHistoricoEntreAlunos() {
        // Arrange
        var bruno = new Aluno("RA2026002", "Bruno Santos", "bruno.santos@email.com");
        var matriculaPaola = poo2025.matricular("MAT-PAOLA", paola);
        matriculaPaola.concluir(ResultadoAcademico.APROVADO);

        // Act
        var matriculaBruno = poo2026.matricular("MAT-BRUNO", bruno);

        // Assert
        assertEquals(1, paola.getMatriculas().size());
        assertEquals(1, bruno.getMatriculas().size());
        assertEquals(poo, matriculaBruno.getOfertaDisciplina().getDisciplina());
    }
}
