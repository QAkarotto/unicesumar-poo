package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SistemaAcademicoTest {

    @Test
    @DisplayName("Deve executar o cenário completo de validação sem violar nenhuma regra")
    void deveExecutarCenarioCompleto() {
        var saidaOriginal = System.out;
        var capturada = new ByteArrayOutputStream();
        System.setOut(new PrintStream(capturada, true, StandardCharsets.UTF_8));

        try {
            assertDoesNotThrow(() -> SistemaAcademico.main(new String[0]));
        } finally {
            System.setOut(saidaOriginal);
        }

        var saida = capturada.toString(StandardCharsets.UTF_8);

        assertTrue(saida.contains("=== VALIDAÇÃO CONCLUÍDA ==="));
        assertEquals(5, saida.split("\\[OK\\]", -1).length - 1);
        assertTrue(saida.contains("já está ofertada para a turma"));
        assertTrue(saida.contains("já foi aprovado em"));
        assertTrue(saida.contains("já possui matrícula em"));
    }
}
