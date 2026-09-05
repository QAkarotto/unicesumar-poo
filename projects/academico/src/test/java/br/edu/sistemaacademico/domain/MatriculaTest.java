package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatriculaTest {

    // Cria um aluno padrão para ser utilizado nos testes
    private Aluno criarAluno() {
        return new Aluno(
                "RA001",
                "Ana Souza",
                "ana@email.com"
        );
    }

    // Cria uma turma e uma oferta de disciplina para os testes
    private OfertaDisciplina criarOferta(Aluno aluno) {
        var turma = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2026, Semestre.SEGUNDO)
        );

        return turma.ofertarDisciplina(
                new Disciplina(
                        "POO",
                        "Programação Orientada a Objetos",
                        80
                )
        );
    }

    @Test
    @DisplayName("Deve criar matrícula com situação ativa")
    void deveCriarMatriculaAtiva() {

        // Cria o aluno e a oferta onde a matrícula será realizada
        var aluno = criarAluno();
        var oferta = criarOferta(aluno);

        // Cria uma nova matrícula vinculada ao aluno e à oferta
        var matricula = new Matricula(
                "MAT-001",
                aluno,
                oferta
        );

        // Verifica se a matrícula começa com a situação ativa
        assertEquals(
                SituacaoMatricula.ATIVA,
                matricula.getSituacao()
        );

        // Verifica se ainda não existe um resultado acadêmico
        assertNull(matricula.getResultado());
    }

    @Test
    @DisplayName("Deve concluir matrícula com resultado aprovado")
    void deveConcluirComoAprovado() {

        // Cria os dados necessários para realizar a matrícula
        var aluno = criarAluno();
        var oferta = criarOferta(aluno);
        var matricula = new Matricula("MAT-001", aluno, oferta);

        // Conclui a matrícula registrando que o aluno foi aprovado
        matricula.concluir(ResultadoAcademico.APROVADO);

        // Verifica se a situação mudou para concluída
        assertEquals(
                SituacaoMatricula.CONCLUIDA,
                matricula.getSituacao()
        );

        // Verifica se o resultado de aprovação foi registrado
        assertEquals(
                ResultadoAcademico.APROVADO,
                matricula.getResultado()
        );
    }

    @Test
    @DisplayName("Deve concluir matrícula com resultado reprovado")
    void deveConcluirComoReprovado() {

        // Cria o aluno, a oferta e a matrícula
        var aluno = criarAluno();
        var oferta = criarOferta(aluno);
        var matricula = new Matricula("MAT-001", aluno, oferta);

        // Conclui a matrícula registrando que o aluno foi reprovado
        matricula.concluir(ResultadoAcademico.REPROVADO);

        // Verifica se a matrícula foi concluída
        assertEquals(
                SituacaoMatricula.CONCLUIDA,
                matricula.getSituacao()
        );

        // Verifica se o resultado de reprovação foi armazenado
        assertEquals(
                ResultadoAcademico.REPROVADO,
                matricula.getResultado()
        );
    }

    @Test
    @DisplayName("Não deve permitir concluir matrícula sem resultado")
    void naoDeveConcluirSemResultado() {

        // Cria uma matrícula que ainda está ativa
        var aluno = criarAluno();
        var oferta = criarOferta(aluno);
        var matricula = new Matricula("MAT-001", aluno, oferta);

        // Tenta concluir a matrícula sem informar um resultado
        assertThrows(
                IllegalArgumentException.class,
                () -> matricula.concluir(null)
        );

        // Confirma que a matrícula continua ativa após o erro
        assertEquals(
                SituacaoMatricula.ATIVA,
                matricula.getSituacao()
        );
    }

    @Test
    @DisplayName("Deve permitir trancar matrícula ativa")
    void deveTrancarMatricula() {

        // Cria uma matrícula ativa
        var aluno = criarAluno();
        var oferta = criarOferta(aluno);
        var matricula = new Matricula("MAT-001", aluno, oferta);

        // Realiza o trancamento da matrícula
        matricula.trancar();

        // Verifica se a situação mudou para trancada
        assertEquals(
                SituacaoMatricula.TRANCADA,
                matricula.getSituacao()
        );
    }

    @Test
    @DisplayName("Deve permitir cancelar matrícula ativa")
    void deveCancelarMatricula() {

        // Cria uma matrícula ativa
        var aluno = criarAluno();
        var oferta = criarOferta(aluno);
        var matricula = new Matricula("MAT-001", aluno, oferta);

        // Realiza o cancelamento da matrícula
        matricula.cancelar();

        // Verifica se a situação mudou para cancelada
        assertEquals(
                SituacaoMatricula.CANCELADA,
                matricula.getSituacao()
        );
    }

    @Test
    @DisplayName("Não deve permitir concluir matrícula já trancada")
    void naoDeveConcluirMatriculaTrancada() {

        // Cria uma matrícula e realiza o seu trancamento
        var aluno = criarAluno();
        var oferta = criarOferta(aluno);
        var matricula = new Matricula("MAT-001", aluno, oferta);
        matricula.trancar();

        // Tenta concluir uma matrícula que não está mais ativa
        assertThrows(
                IllegalStateException.class,
                () -> matricula.concluir(ResultadoAcademico.APROVADO)
        );
    }

    @Test
    @DisplayName("Não deve permitir cancelar matrícula já trancada")
    void naoDeveCancelarMatriculaTrancada() {

        // Cria uma matrícula e coloca ela como trancada
        var aluno = criarAluno();
        var oferta = criarOferta(aluno);
        var matricula = new Matricula("MAT-001", aluno, oferta);
        matricula.trancar();

        // Tenta cancelar a matrícula depois do trancamento
        assertThrows(
                IllegalStateException.class,
                matricula::cancelar
        );
    }

    @Test
    @DisplayName("Não deve permitir concluir matrícula já cancelada")
    void naoDeveConcluirMatriculaCancelada() {

        // Cria uma matrícula e realiza o seu cancelamento
        var aluno = criarAluno();
        var oferta = criarOferta(aluno);
        var matricula = new Matricula("MAT-001", aluno, oferta);
        matricula.cancelar();

        // Tenta concluir uma matrícula que já foi cancelada
        assertThrows(
                IllegalStateException.class,
                () -> matricula.concluir(ResultadoAcademico.APROVADO)
        );
    }

    @Test
    @DisplayName("Não deve permitir trancar matrícula já cancelada")
    void naoDeveTrancarMatriculaCancelada() {

        // Cria uma matrícula e cancela a matrícula
        var aluno = criarAluno();
        var oferta = criarOferta(aluno);
        var matricula = new Matricula("MAT-001", aluno, oferta);
        matricula.cancelar();

        // Tenta trancar uma matrícula que já foi cancelada
        assertThrows(
                IllegalStateException.class,
                matricula::trancar
        );
    }

    @Test
    @DisplayName("Não deve permitir alterar uma matrícula concluída")
    void naoDeveAlterarMatriculaConcluida() {

        // Cria uma matrícula e registra a aprovação do aluno
        var aluno = criarAluno();
        var oferta = criarOferta(aluno);
        var matricula = new Matricula("MAT-001", aluno, oferta);
        matricula.concluir(ResultadoAcademico.APROVADO);

        // Tenta trancar uma matrícula que já foi concluída
        assertThrows(
                IllegalStateException.class,
                matricula::trancar
        );

        // Tenta cancelar uma matrícula que já foi concluída
        assertThrows(
                IllegalStateException.class,
                matricula::cancelar
        );
    }

    @Test
    @DisplayName("Deve permitir nova matrícula após reprovação")
    void devePermitirNovaMatriculaAposReprovacao() {

        // Cria o aluno e a disciplina que será utilizada nas duas tentativas
        var aluno = criarAluno();

        var disciplina = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        // Cria a primeira oferta da disciplina
        var primeiraTurma = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2026, Semestre.SEGUNDO)
        );

        var primeiraOferta = primeiraTurma.ofertarDisciplina(disciplina);

        // Realiza a primeira matrícula
        var primeiraMatricula = primeiraOferta.matricular(aluno);

        // Registra que o aluno foi reprovado na primeira tentativa
        primeiraMatricula.concluir(ResultadoAcademico.REPROVADO);

        // Cria uma nova oferta da mesma disciplina
        var segundaTurma = new Turma(
                "ESOFT4S-NB",
                new PeriodoLetivo(2027, Semestre.SEGUNDO)
        );

        var segundaOferta = segundaTurma.ofertarDisciplina(disciplina);

        // Realiza uma nova matrícula na nova oferta
        var segundaMatricula = segundaOferta.matricular(aluno);

        // Confirma que a primeira matrícula ficou concluída
        assertEquals(
                SituacaoMatricula.CONCLUIDA,
                primeiraMatricula.getSituacao()
        );

        // Confirma que o resultado da primeira matrícula foi reprovação
        assertEquals(
                ResultadoAcademico.REPROVADO,
                primeiraMatricula.getResultado()
        );

        // Confirma que a nova matrícula começou ativa
        assertEquals(
                SituacaoMatricula.ATIVA,
                segundaMatricula.getSituacao()
        );

        // Confirma que as duas matrículas foram registradas no aluno
        assertEquals(2, aluno.getMatriculas().size());

        // Confirma que cada oferta possui sua própria matrícula
        assertEquals(1, primeiraOferta.getMatriculas().size());
        assertEquals(1, segundaOferta.getMatriculas().size());
    }

    @Test
    @DisplayName("Não deve permitir nova matrícula após aprovação")
    void naoDevePermitirNovaMatriculaAposAprovacao() {

        // Cria o aluno e a disciplina que será utilizada nas duas ofertas
        var aluno = criarAluno();

        var disciplina = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        // Cria a primeira turma e oferta da disciplina
        var primeiraTurma = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2026, Semestre.SEGUNDO)
        );

        var primeiraOferta = primeiraTurma.ofertarDisciplina(disciplina);

        // Realiza a primeira matrícula
        var primeiraMatricula = primeiraOferta.matricular(aluno);

        // Registra que o aluno foi aprovado na disciplina
        primeiraMatricula.concluir(ResultadoAcademico.APROVADO);

        // Cria uma nova turma e uma nova oferta da mesma disciplina
        var segundaTurma = new Turma(
                "ESOFT4S-NB",
                new PeriodoLetivo(2027, Semestre.SEGUNDO)
        );

        var segundaOferta = segundaTurma.ofertarDisciplina(disciplina);

        // Tenta matricular novamente o aluno em uma nova oferta
        // da disciplina em que ele já foi aprovado
        assertThrows(
                IllegalStateException.class,
                () -> segundaOferta.matricular(aluno)
        );

        // Confirma que nenhuma nova matrícula foi criada
        assertEquals(1, aluno.getMatriculas().size());
        assertEquals(1, primeiraOferta.getMatriculas().size());
        assertEquals(0, segundaOferta.getMatriculas().size());
    }
}