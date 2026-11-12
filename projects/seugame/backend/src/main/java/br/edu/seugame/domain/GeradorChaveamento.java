package br.edu.seugame.domain;

import java.util.ArrayList;
import java.util.List;

public class GeradorChaveamento implements GeradorCompeticao {
    @Override public TipoTorneio tipo() { return TipoTorneio.CHAVEAMENTO; }

    static int tamanhoChave(int participantes) {
        int tamanho = 1;
        while (tamanho < participantes) tamanho = Math.multiplyExact(tamanho, 2);
        return tamanho;
    }

    @Override
    public List<Partida> gerar(Torneio torneio) {
        var participantes = preparar(torneio);
        int tamanho = tamanhoChave(participantes.size());
        int iniciais = tamanho / 2;
        Participante[] vagas = new Participante[tamanho];
        for (int i = 0; i < participantes.size(); i++) {
            int slot = i < iniciais ? i * 2 : (i - iniciais) * 2 + 1;
            vagas[slot] = participantes.get(i);
        }
        var geradas = new ArrayList<Partida>();
        for (int rodada = 1, jogos = iniciais; jogos > 0; rodada++, jogos /= 2) {
            for (int ordem = 1; ordem <= jogos; ordem++) {
                geradas.add(new Partida(torneio,
                        rodada == 1 ? vagas[(ordem - 1) * 2] : null,
                        rodada == 1 ? vagas[(ordem - 1) * 2 + 1] : null,
                        rodada, ordem, FasePartida.CHAVE));
            }
        }
        new ReconstrutorChaveamento().reconstruir(torneio);
        return List.copyOf(geradas);
    }
}
