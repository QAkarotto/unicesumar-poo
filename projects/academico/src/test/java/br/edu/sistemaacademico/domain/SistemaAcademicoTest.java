package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SistemaAcademicoTest {

    @Test
    @DisplayName("Fluxo principal deve executar até a validação final sem erro")
    void deveExecutarFluxoPrincipalSemErro() {
        var saida = capturarSaida(() ->
                assertDoesNotThrow(() -> SistemaAcademico.main(new String[0]))
        );

        assertTrue(saida.contains("=== OFERTA DE DISCIPLINAS ==="));
        assertTrue(saida.contains("=== DISCIPLINA DUPLICADA ==="));
        assertTrue(saida.contains("=== PRIMEIRA MATRÍCULA ==="));
        assertTrue(saida.contains("=== NOVA MATRÍCULA APÓS REPROVAÇÃO ==="));
        assertTrue(saida.contains("=== MATRÍCULA APÓS APROVAÇÃO ==="));
        assertTrue(saida.contains("=== MATRÍCULA DUPLICADA ==="));
        assertTrue(saida.contains("=== CONSULTAS ==="));
        assertTrue(saida.contains("=== VALIDAÇÃO CONCLUÍDA ==="));

        assertTrue(saida.contains("A turma deve permitir várias disciplinas ofertadas."));
        assertTrue(saida.contains("A mesma disciplina não pode ser ofertada duas vezes na mesma turma."));
        assertTrue(saida.contains("Aluno aprovado não pode cursar novamente a mesma disciplina."));
        assertTrue(saida.contains("Aluno não pode possuir duas matrículas na mesma oferta."));
        assertTrue(saida.contains("[OK]"));
    }

    @Test
    @DisplayName("Main deve aceitar array de argumentos nulo porque não utiliza argumentos")
    void deveExecutarMainComArgumentosNulos() {
        var saida = capturarSaida(() ->
                assertDoesNotThrow(() -> SistemaAcademico.main(null))
        );

        assertTrue(saida.contains("=== VALIDAÇÃO CONCLUÍDA ==="));
    }

    @Test
    @DisplayName("Validação interna deve imprimir OK quando a condição for verdadeira")
    void deveAceitarValidacaoVerdadeira() throws Exception {
        Method validar = metodoValidar();

        var saida = capturarSaida(() ->
                assertDoesNotThrow(() -> validar.invoke(null, true, "Condição válida"))
        );

        assertTrue(saida.contains("[OK] Condição válida"));
    }

    @Test
    @DisplayName("Validação interna deve lançar AssertionError quando a condição for falsa")
    void deveFalharQuandoValidacaoForFalsa() throws Exception {
        Method validar = metodoValidar();

        var excecao = assertThrows(
                InvocationTargetException.class,
                () -> validar.invoke(null, false, "Condição inválida")
        );

        var causa = assertInstanceOf(AssertionError.class, excecao.getCause());
        assertEquals("Condição inválida", causa.getMessage());
    }

    @Test
    @DisplayName("Esperar falha deve tratar IllegalArgumentException como operação corretamente impedida")
    void deveTratarIllegalArgumentExceptionComoFalhaEsperada() throws Exception {
        Method esperarFalha = metodoEsperarFalha();

        Runnable operacao = () -> {
            throw new IllegalArgumentException("dado inválido");
        };

        var saida = capturarSaida(() ->
                assertDoesNotThrow(() -> esperarFalha.invoke(
                        null,
                        "Operação inválida deve ser impedida.",
                        operacao
                ))
        );

        assertTrue(saida.contains("[OK] Operação inválida deve ser impedida."));
        assertTrue(saida.contains("Motivo: dado inválido"));
    }

    @Test
    @DisplayName("Esperar falha deve tratar IllegalStateException como operação corretamente impedida")
    void deveTratarIllegalStateExceptionComoFalhaEsperada() throws Exception {
        Method esperarFalha = metodoEsperarFalha();

        Runnable operacao = () -> {
            throw new IllegalStateException("estado inválido");
        };

        var saida = capturarSaida(() ->
                assertDoesNotThrow(() -> esperarFalha.invoke(
                        null,
                        "Estado inválido deve ser impedido.",
                        operacao
                ))
        );

        assertTrue(saida.contains("[OK] Estado inválido deve ser impedido."));
        assertTrue(saida.contains("Motivo: estado inválido"));
    }

    @Test
    @DisplayName("Esperar falha deve acusar erro quando a operação que deveria falhar é permitida")
    void deveAcusarErroQuandoOperacaoInvalidaNaoFalhar() throws Exception {
        Method esperarFalha = metodoEsperarFalha();

        Runnable operacaoPermitida = () -> {
            // Não lança exceção de propósito: o helper deve detectar que a operação não falhou.
        };

        var excecao = assertThrows(
                InvocationTargetException.class,
                () -> esperarFalha.invoke(
                        null,
                        "A operação deveria falhar.",
                        operacaoPermitida
                )
        );

        var causa = assertInstanceOf(AssertionError.class, excecao.getCause());
        assertEquals(
                "A operação deveria ter sido impedida: A operação deveria falhar.",
                causa.getMessage()
        );
    }

    private static Method metodoValidar() throws NoSuchMethodException {
        var metodo = SistemaAcademico.class.getDeclaredMethod(
                "validar",
                boolean.class,
                String.class
        );
        metodo.setAccessible(true);
        return metodo;
    }

    private static Method metodoEsperarFalha() throws NoSuchMethodException {
        var metodo = SistemaAcademico.class.getDeclaredMethod(
                "esperarFalha",
                String.class,
                Runnable.class
        );
        metodo.setAccessible(true);
        return metodo;
    }

    private static String capturarSaida(Runnable acao) {
        var saidaOriginal = System.out;
        var buffer = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            acao.run();
        } finally {
            System.setOut(saidaOriginal);
        }

        return buffer.toString(StandardCharsets.UTF_8);
    }
}
