package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MatriculaTest {

    @Test
    @DisplayName("A matricula deve nascer ativa e sem resultado")
    void aMatriculaDeveNascerAtivaESemResultado() {
        // Arrange
        Turma turma = new Turma("ADSIS4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        OfertaDisciplina oferta = turma.ofertarDisciplina(
                new Disciplina("POO", "Programacao Orientada a Objetos", 80)
        );
        Aluno aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");

        // Act
        Matricula matricula = new Matricula("MAT-001", aluno, oferta);

        // Assert
        assertEquals("MAT-001", matricula.getCodigo());
        assertEquals(aluno, matricula.getAluno());
        assertEquals(oferta, matricula.getOfertaDisciplina());
        assertEquals(turma, matricula.getTurma());
        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
        assertNull(matricula.getResultado());
        assertEquals("MAT-001 - RA001 - POO - ATIVA", matricula.toString());
    }

    @Test
    @DisplayName("Nao deve criar matricula sem codigo")
    void naoDeveCriarMatriculaSemCodigo() {
        // Arrange
        Turma turma = new Turma("ADSIS4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        OfertaDisciplina oferta = turma.ofertarDisciplina(
                new Disciplina("POO", "Programacao Orientada a Objetos", 80)
        );
        Aluno aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Matricula("   ", aluno, oferta);
        });
    }

    @Test
    @DisplayName("Nao deve criar matricula sem aluno")
    void naoDeveCriarMatriculaSemAluno() {
        // Arrange
        Turma turma = new Turma("ADSIS4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        OfertaDisciplina oferta = turma.ofertarDisciplina(
                new Disciplina("POO", "Programacao Orientada a Objetos", 80)
        );

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Matricula("MAT-001", null, oferta);
        });
    }

    @Test
    @DisplayName("Nao deve criar matricula sem oferta de disciplina")
    void naoDeveCriarMatriculaSemOfertaDeDisciplina() {
        // Arrange
        Aluno aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");
        OfertaDisciplina ofertaNula = null;

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Matricula("MAT-001", aluno, ofertaNula);
        });
    }

    @Test
    @DisplayName("Deve matricular usando a turma quando ela tem uma unica oferta")
    void deveMatricularUsandoATurmaQuandoElaTemUmaUnicaOferta() {
        // Arrange
        Turma turma = new Turma(
                "ADSIS4S",
                new Disciplina("POO", "Programacao Orientada a Objetos", 80),
                new PeriodoLetivo(2026, Semestre.SEGUNDO)
        );
        Aluno aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");

        // Act
        Matricula matricula = new Matricula("MAT-001", aluno, turma);

        // Assert
        assertEquals(turma, matricula.getTurma());
        assertEquals(1, aluno.getMatriculas().size());
    }

    @Test
    @DisplayName("Nao deve matricular sem turma")
    void naoDeveMatricularSemTurma() {
        // Arrange
        Aluno aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");
        Turma turmaNula = null;

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Matricula("MAT-001", aluno, turmaNula);
        });
    }

    @Test
    @DisplayName("Nao deve matricular em turma que nao tem oferta")
    void naoDeveMatricularEmTurmaQueNaoTemOferta() {
        // Arrange
        Turma turma = new Turma("ADSIS4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        Aluno aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");

        // Act / Assert
        assertThrows(IllegalStateException.class, () -> {
            new Matricula("MAT-001", aluno, turma);
        });
    }

    @Test
    @DisplayName("Nao deve matricular em turma com mais de uma oferta")
    void naoDeveMatricularEmTurmaComMaisDeUmaOferta() {
        // Arrange
        Turma turma = new Turma("ADSIS4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        turma.ofertarDisciplina(new Disciplina("POO", "Programacao Orientada a Objetos", 80));
        turma.ofertarDisciplina(new Disciplina("BD", "Banco de Dados", 60));
        Aluno aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");

        // Act / Assert
        assertThrows(IllegalStateException.class, () -> {
            new Matricula("MAT-001", aluno, turma);
        });
    }

    @Test
    @DisplayName("Deve concluir a matricula como aprovado")
    void deveConcluirAMatriculaComoAprovado() {
        // Arrange
        Turma turma = new Turma("ADSIS4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        OfertaDisciplina oferta = turma.ofertarDisciplina(
                new Disciplina("POO", "Programacao Orientada a Objetos", 80)
        );
        Matricula matricula = oferta.matricular(
                new Aluno("RA001", "Ana Souza", "ana@email.com")
        );

        // Act
        matricula.concluir(ResultadoAcademico.APROVADO);

        // Assert
        assertEquals(SituacaoMatricula.CONCLUIDA, matricula.getSituacao());
        assertEquals(ResultadoAcademico.APROVADO, matricula.getResultado());
    }

    @Test
    @DisplayName("Deve concluir a matricula como reprovado")
    void deveConcluirAMatriculaComoReprovado() {
        // Arrange
        Turma turma = new Turma("ADSIS4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        OfertaDisciplina oferta = turma.ofertarDisciplina(
                new Disciplina("POO", "Programacao Orientada a Objetos", 80)
        );
        Matricula matricula = oferta.matricular(
                new Aluno("RA001", "Ana Souza", "ana@email.com")
        );

        // Act
        matricula.concluir(ResultadoAcademico.REPROVADO);

        // Assert
        assertEquals(SituacaoMatricula.CONCLUIDA, matricula.getSituacao());
        assertEquals(ResultadoAcademico.REPROVADO, matricula.getResultado());
    }

    @Test
    @DisplayName("Nao deve concluir a matricula sem informar o resultado")
    void naoDeveConcluirAMatriculaSemInformarOResultado() {
        // Arrange
        Turma turma = new Turma("ADSIS4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        OfertaDisciplina oferta = turma.ofertarDisciplina(
                new Disciplina("POO", "Programacao Orientada a Objetos", 80)
        );
        Matricula matricula = oferta.matricular(
                new Aluno("RA001", "Ana Souza", "ana@email.com")
        );

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> {
            matricula.concluir(null);
        });
        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
    }

    @Test
    @DisplayName("Deve trancar uma matricula ativa")
    void deveTrancarUmaMatriculaAtiva() {
        // Arrange
        Turma turma = new Turma("ADSIS4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        OfertaDisciplina oferta = turma.ofertarDisciplina(
                new Disciplina("POO", "Programacao Orientada a Objetos", 80)
        );
        Matricula matricula = oferta.matricular(
                new Aluno("RA001", "Ana Souza", "ana@email.com")
        );

        // Act
        matricula.trancar();

        // Assert
        assertEquals(SituacaoMatricula.TRANCADA, matricula.getSituacao());
    }

    @Test
    @DisplayName("Deve cancelar uma matricula ativa")
    void deveCancelarUmaMatriculaAtiva() {
        // Arrange
        Turma turma = new Turma("ADSIS4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        OfertaDisciplina oferta = turma.ofertarDisciplina(
                new Disciplina("POO", "Programacao Orientada a Objetos", 80)
        );
        Matricula matricula = oferta.matricular(
                new Aluno("RA001", "Ana Souza", "ana@email.com")
        );

        // Act
        matricula.cancelar();

        // Assert
        assertEquals(SituacaoMatricula.CANCELADA, matricula.getSituacao());
    }

    @Test
    @DisplayName("Nao deve trancar uma matricula que ja esta trancada")
    void naoDeveTrancarUmaMatriculaQueJaEstaTrancada() {
        // Arrange
        Turma turma = new Turma("ADSIS4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        OfertaDisciplina oferta = turma.ofertarDisciplina(
                new Disciplina("POO", "Programacao Orientada a Objetos", 80)
        );
        Matricula matricula = oferta.matricular(
                new Aluno("RA001", "Ana Souza", "ana@email.com")
        );
        matricula.trancar();

        // Act / Assert
        assertThrows(IllegalStateException.class, () -> {
            matricula.trancar();
        });
    }

    @Test
    @DisplayName("Nao deve concluir uma matricula cancelada")
    void naoDeveConcluirUmaMatriculaCancelada() {
        // Arrange
        Turma turma = new Turma("ADSIS4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        OfertaDisciplina oferta = turma.ofertarDisciplina(
                new Disciplina("POO", "Programacao Orientada a Objetos", 80)
        );
        Matricula matricula = oferta.matricular(
                new Aluno("RA001", "Ana Souza", "ana@email.com")
        );
        matricula.cancelar();

        // Act / Assert
        assertThrows(IllegalStateException.class, () -> {
            matricula.concluir(ResultadoAcademico.APROVADO);
        });
    }

    @Test
    @DisplayName("Deve permitir nova matricula na mesma disciplina depois de reprovado")
    void devePermitirNovaMatriculaNaMesmaDisciplinaDepoisDeReprovado() {
        // Arrange
        Aluno aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");

        Turma turmaAtual = new Turma("ADSIS4S", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        OfertaDisciplina ofertaAtual = turmaAtual.ofertarDisciplina(
                new Disciplina("POO", "Programacao Orientada a Objetos", 80)
        );
        Matricula primeiraMatricula = ofertaAtual.matricular(aluno);
        primeiraMatricula.concluir(ResultadoAcademico.REPROVADO);

        Turma proximaTurma = new Turma("ADSIS5S", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        OfertaDisciplina proximaOferta = proximaTurma.ofertarDisciplina(
                new Disciplina("POO", "Programacao Orientada a Objetos", 80)
        );

        // Act
        Matricula segundaMatricula = proximaOferta.matricular(aluno);

        // Assert
        assertEquals(SituacaoMatricula.ATIVA, segundaMatricula.getSituacao());
        assertEquals(2, aluno.getMatriculas().size());
    }

    @Test
    @DisplayName("Nao deve permitir nova matricula na disciplina em que o aluno foi aprovado")
    void naoDevePermitirNovaMatriculaNaDisciplinaEmQueOAlunoFoiAprovado() {
        // Arrange
        Aluno aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");

        Turma turmaAtual = new Turma("ADSIS4S", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        OfertaDisciplina ofertaAtual = turmaAtual.ofertarDisciplina(
                new Disciplina("POO", "Programacao Orientada a Objetos", 80)
        );
        Matricula primeiraMatricula = ofertaAtual.matricular(aluno);
        primeiraMatricula.concluir(ResultadoAcademico.APROVADO);

        Turma proximaTurma = new Turma("ADSIS5S", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        OfertaDisciplina proximaOferta = proximaTurma.ofertarDisciplina(
                new Disciplina("POO", "Programacao Orientada a Objetos", 80)
        );

        // Act / Assert
        assertThrows(IllegalStateException.class, () -> {
            proximaOferta.matricular(aluno);
        });
        assertEquals(1, aluno.getMatriculas().size());
    }

    @Test
    @DisplayName("Ser aprovado em uma disciplina nao impede matricula em outra")
    void serAprovadoEmUmaDisciplinaNaoImpedeMatriculaEmOutra() {
        // Arrange
        Aluno aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");
        Turma turma = new Turma("ADSIS4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));

        OfertaDisciplina ofertaDePoo = turma.ofertarDisciplina(
                new Disciplina("POO", "Programacao Orientada a Objetos", 80)
        );
        Matricula matriculaDePoo = ofertaDePoo.matricular(aluno);
        matriculaDePoo.concluir(ResultadoAcademico.APROVADO);

        OfertaDisciplina ofertaDeBanco = turma.ofertarDisciplina(
                new Disciplina("BD", "Banco de Dados", 60)
        );

        // Act
        Matricula matriculaDeBanco = ofertaDeBanco.matricular(aluno);

        // Assert
        assertEquals(SituacaoMatricula.ATIVA, matriculaDeBanco.getSituacao());
        assertEquals(2, aluno.getMatriculas().size());
    }
}
