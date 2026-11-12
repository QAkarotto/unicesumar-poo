package br.edu.seugame.domain;

import java.util.ArrayList;
import java.util.List;

public class GeradorTabela implements GeradorCompeticao {
    @Override public TipoTorneio tipo() { return TipoTorneio.TABELA; }

    @Override
    public List<Partida> gerar(Torneio torneio) {
        var vagas = new ArrayList<>(preparar(torneio));
        if (vagas.size() % 2 != 0) vagas.add(null);
        int total = vagas.size();
        var geradas = new ArrayList<Partida>();
        for (int rodada = 1; rodada < total; rodada++) {
            int ordem = 1;
            for (int i = 0; i < total / 2; i++) {
                Participante primeiro = vagas.get(i), segundo = vagas.get(total - 1 - i);
                if (primeiro == null || segundo == null) continue;
                if (rodada % 2 == 0 && i == 0) {
                    Participante temporario = primeiro; primeiro = segundo; segundo = temporario;
                }
                geradas.add(new Partida(torneio, primeiro, segundo, rodada, ordem++, FasePartida.LIGA));
            }
            vagas.add(1, vagas.removeLast());
        }
        return List.copyOf(geradas);
    }
}
