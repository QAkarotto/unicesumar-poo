package br.com.vetagenda.domain;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class AgendamentoTest {
    @Test
    void deveCancelarAgendamentoAtivo() {
        var especialidade = new Especialidade("Clínica geral", "Consultas gerais");
        var veterinario = new Veterinario("Dra. Ana", "CRMV-123", "41999999999", especialidade);
        var tutor = new Tutor("João", "12345678900", "41988888888", "joao@email.com");
        var animal = new Animal("Rex", "Cachorro", "SRD", "Macho",
            null, null, tutor);
        var agendamento = new Agendamento(LocalDateTime.now().plusDays(1),
            "Consulta", null, animal, veterinario);

        agendamento.cancelar();

        assertEquals(StatusAgendamento.CANCELADO, agendamento.getStatus());
    }

    @Test
    void naoDeveCancelarAgendamentoJaRealizado() {
        var especialidade = new Especialidade("Clínica geral", "Consultas gerais");
        var veterinario = new Veterinario("Dra. Ana", "CRMV-123", "41999999999", especialidade);
        var tutor = new Tutor("João", "12345678900", "41988888888", "joao@email.com");
        var animal = new Animal("Rex", "Cachorro", "SRD", "Macho", null, null, tutor);
        var agendamento = new Agendamento(LocalDateTime.now().plusDays(1),
            "Consulta", null, animal, veterinario);
        agendamento.marcarComoRealizado();

        assertThrows(IllegalStateException.class, agendamento::cancelar);
    }

    @Test
    void naoDeveRealizarAgendamentoCancelado() {
        var especialidade = new Especialidade("Clínica geral", "Consultas gerais");
        var veterinario = new Veterinario("Dra. Ana", "CRMV-123", "41999999999", especialidade);
        var tutor = new Tutor("João", "12345678900", "41988888888", "joao@email.com");
        var animal = new Animal("Rex", "Cachorro", "SRD", "Macho", null, null, tutor);
        var agendamento = new Agendamento(LocalDateTime.now().plusDays(1),
            "Consulta", null, animal, veterinario);
        agendamento.cancelar();

        assertThrows(IllegalStateException.class, agendamento::marcarComoRealizado);
    }
}
