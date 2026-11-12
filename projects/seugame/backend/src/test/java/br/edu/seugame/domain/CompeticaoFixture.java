package br.edu.seugame.domain;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

final class CompeticaoFixture {
    private CompeticaoFixture() {}
    static Torneio criar(TipoTorneio tipo, int quantidade) {
        var t = new Torneio(new Usuario("Organizador", "organizador", "a@example.invalid", "hash"),
                "Torneio teste", "", tipo, LocalDate.of(2030, 1, 1),
                Clock.fixed(Instant.parse("2026-11-08T12:00:00Z"), ZoneOffset.UTC));
        for (int i = 1; i <= quantidade; i++) {
            var p = new Participante(t, "Participante " + i, i);
            try {
                var campo = Participante.class.getDeclaredField("id");
                campo.setAccessible(true); campo.set(p, (long) i);
            } catch (ReflectiveOperationException e) { throw new AssertionError(e); }
        }
        return t;
    }
}
