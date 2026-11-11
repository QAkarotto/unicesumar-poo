package br.edu.agroassist.domain;

import br.edu.agroassist.exception.DadosInvalidosException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

class PropriedadeTest {

    private final Usuario produtor =
            new Usuario(
                    "u1",
                    "Ana Silva",
                    Perfil.PRODUTOR,
                    "(42) 99999-0000",
                    "ana@exemplo.com",
                    "Castro",
                    LocalDateTime.of(2026, 10, 1, 9, 0));
    private final Cultura soja = new Cultura("c1", "Soja");

    @Test
    void naoCadastraPropriedadeSemAreaPositiva() {
        assertThrows(
                DadosInvalidosException.class,
                () ->
                        new Propriedade(
                                "p1", produtor, "Sítio Esperança", "Castro", BigDecimal.ZERO, Set.of(soja)));
    }

    @Test
    void naoCadastraPropriedadeSemCulturas() {
        assertThrows(
                DadosInvalidosException.class,
                () ->
                        new Propriedade(
                                "p1",
                                produtor,
                                "Sítio Esperança",
                                "Castro",
                                new BigDecimal("10.5"),
                                Set.of()));
    }
}
