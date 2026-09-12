package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SistemaAcademicoTest {

    private SistemaAcademico sistema;

    @BeforeEach
    void setUp() {
        sistema = new SistemaAcademico();
    }

    @Test
    void deveCalcularMediaCorretamente() {
        double media = sistema.calcularMedia(6.0, 7.0, 8.0);
        // O terceiro parâmetro (0.001) é a margem de tolerância para comparação de double
        assertEquals(7.0, media, 0.001);
    }

    @Test
    void deveRetornarReprovadoPorFaltaQuandoFaltasMaiorQue20() {
        // Mesmo com média máxima, se passar de 20 faltas, reprova direto
        String status = sistema.verificarStatus(10.0, 21);
        assertEquals("REPROVADO_POR_FALTA", status);
    }

    @Test
    void deveRetornarAprovadoQuandoMediaMaiorOuIgualA6EFaltasPermitidas() {
        String statusExato = sistema.verificarStatus(6.0, 15);
        assertEquals("APROVADO", statusExato);

        String statusMaior = sistema.verificarStatus(8.5, 20);
        assertEquals("APROVADO", statusMaior);
    }

    @Test
    void deveRetornarExameQuandoMediaMenorQue6EFaltasPermitidas() {
        String status = sistema.verificarStatus(5.9, 15);
        assertEquals("EXAME", status);
    }

    @Test
    void deveGerarOrientacaoParaAprovado() {
        String orientacao = sistema.gerarOrientacao("APROVADO");
        assertEquals("Parabéns! Você dominou Classes e Objetos. Boas férias!", orientacao);
    }

    @Test
    void deveGerarOrientacaoParaExame() {
        String orientacao = sistema.gerarOrientacao("EXAME");
        assertEquals("Atenção: Estude os conceitos de Herança e Polimorfismo para a prova substitutiva.", orientacao);
    }

    @Test
    void deveGerarOrientacaoParaReprovadoPorFalta() {
        String orientacao = sistema.gerarOrientacao("REPROVADO_POR_FALTA");
        assertEquals("Reprovação automática. Frequência abaixo do mínimo exigido.", orientacao);
    }

    @Test
    void deveGerarOrientacaoParaStatusInvalido() {
        String orientacao = sistema.gerarOrientacao("STATUS_INEXISTENTE");
        assertEquals("Procure a coordenação do curso.", orientacao);
    }
}