package br.edu.seugame.domain;

import java.util.Comparator;
import java.util.List;

public interface GeradorCompeticao {
    TipoTorneio tipo();
    List<Partida> gerar(Torneio torneio);

    default List<Participante> preparar(Torneio torneio) {
        torneio.exigirEditavel();
        if (torneio.getTipo() != tipo()) throw new IllegalArgumentException("Formato incompatível com o gerador.");
        torneio.validarParticipantes();
        if (!torneio.getPartidas().isEmpty()) throw new OperacaoNaoPermitidaException("A competição já possui partidas.");
        var participantes = torneio.getParticipantes().stream()
                .sorted(Comparator.comparingInt(Participante::getOrdemSorteio)).toList();
        if (participantes.stream().anyMatch(p -> p.getId() == null))
            throw new OperacaoNaoPermitidaException("Persista todos os participantes antes de gerar a competição.");
        return participantes;
    }
}
