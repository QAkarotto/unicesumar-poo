package br.edu.seugame.service;

import br.edu.seugame.domain.*;
import br.edu.seugame.dto.TorneioDtos.*;
import br.edu.seugame.exception.*;
import br.edu.seugame.repository.*;
import br.edu.seugame.security.SessaoUsuario;
import java.time.*;
import java.util.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Profile("!infra") @Transactional(readOnly=true)
public class TorneioService {
    private final UsuarioRepository usuarios;
    private final TorneioRepository torneios;
    private final ParticipanteRepository participantes;
    private final PartidaRepository partidas;
    private final ClassificacaoRepository classificacoes;
    private final Clock relogio;
    public TorneioService(UsuarioRepository usuarios,TorneioRepository torneios,ParticipanteRepository participantes,
            PartidaRepository partidas,ClassificacaoRepository classificacoes,Clock relogio) {
        this.usuarios=usuarios;this.torneios=torneios;this.participantes=participantes;
        this.partidas=partidas;this.classificacoes=classificacoes;this.relogio=relogio;
    }
    public List<ResumoResponse> listar(String busca,TipoTorneio tipo,StatusTorneio status) {
        return torneios.findAll(TorneioFiltro.por(busca,tipo,status,LocalDate.now(relogio))).stream().map(this::resumo)
                .sorted(Comparator.comparingInt((ResumoResponse t) -> prioridade(t.status()))
                        .thenComparing(ResumoResponse::dataInicio,Comparator.reverseOrder())
                        .thenComparing(ResumoResponse::id,Comparator.reverseOrder())).toList();
    }
    private int prioridade(StatusTorneio status) {return switch(status){case ANDAMENTO->0;case FUTURO->1;case FINALIZADO->2;};}
    public TorneioResponse consultar(Long id) { return resposta(buscar(id)); }
    public List<PartidaResponse> partidas(Long id) {
        buscar(id);
        return partidas.findByTorneioIdOrderByRodadaAscOrdemAsc(id).stream().map(this::partida).toList();
    }
    public List<ClassificacaoResponse> classificacao(Long id) {
        Torneio t=buscar(id);exigirFormato(t,TipoTorneio.TABELA);
        var linhas=new ClassificadorTabela().ordenar(classificacoes.findByTorneioId(id));
        var resposta=new ArrayList<ClassificacaoResponse>();
        for(Classificacao c:linhas)resposta.add(new ClassificacaoResponse(resposta.size()+1,participante(c.getParticipante()),
                c.getJogos(),c.getVitorias(),c.getEmpates(),c.getDerrotas(),c.getGolsPro(),c.getGolsContra(),c.getSaldo(),c.getPontos()));
        return List.copyOf(resposta);
    }
    public List<RodadaResponse> chaveamento(Long id) {
        Torneio t=buscar(id);exigirFormato(t,TipoTorneio.CHAVEAMENTO);
        var jogos=partidas.findByTorneioIdOrderByRodadaAscOrdemAsc(id);
        int max=jogos.stream().mapToInt(Partida::getRodada).max().orElse(1);
        var rodadas=new LinkedHashMap<Integer,List<PartidaResponse>>();
        jogos.forEach(p -> rodadas.computeIfAbsent(p.getRodada(),r->new ArrayList<>()).add(partida(p)));
        return rodadas.entrySet().stream().map(e->new RodadaResponse(e.getKey(),nomeFase(e.getKey(),max),List.copyOf(e.getValue()))).toList();
    }
    private String nomeFase(int rodada,int max) {return switch(max-rodada){case 0->"Final";case 1->"Semifinal";case 2->"Quartas de final";case 3->"Oitavas de final";default->rodada+"ª fase";};}

    @Transactional
    public TorneioResponse criar(TorneioRequest request) {
        var nomes=Participante.validarNomes(request.participantes());
        var criador=usuarios.findById(SessaoUsuario.id()).orElseThrow(()->new RecursoNaoEncontradoException("Usuário não encontrado."));
        Torneio t=torneios.saveAndFlush(new Torneio(criador,request.nome(),request.descricao(),request.tipo(),request.dataInicio(),relogio));
        criarParticipantes(t,nomes);gerar(t);return resposta(t);
    }
    @Transactional
    public TorneioResponse editar(Long id,TorneioRequest request) {
        Torneio t=autorParaAlterar(id);t.exigirEditavel();
        var nomes=Participante.validarNomes(request.participantes());
        boolean mudouLista=!new HashSet<>(nomes).equals(new HashSet<>(t.getParticipantes().stream().map(Participante::getNome).toList()));
        boolean mudouFormato=request.tipo()!=t.getTipo();
        if(mudouLista && !t.podeAlterarParticipantes())throw new ConflitoException("Participantes bloqueados após resultados.");
        if(mudouFormato)t.alterarFormato(request.tipo(),relogio);
        t.alterarDadosBasicos(request.nome(),request.descricao(),request.dataInicio(),relogio);
        if(mudouLista || mudouFormato) {
            removerEstrutura(t,mudouLista,false);
            if(mudouLista)criarParticipantes(t,nomes);
            gerar(t);
        }
        torneios.flush();return resposta(t);
    }
    @Transactional
    public void excluir(Long id) {
        Torneio t=autorParaAlterar(id);t.validarExclusao();removerEstrutura(t,true,true);
        torneios.delete(t);torneios.flush();
    }
    @Transactional
    public List<PartidaResponse> registrarResultado(Long id,Long partidaId,ResultadoRequest request) {
        Torneio t=autorParaAlterar(id);t.exigirEditavel();
        Partida p=t.getPartidas().stream().filter(j->j.getId().equals(partidaId)).findFirst()
                .orElseThrow(()->new RecursoNaoEncontradoException("Partida não encontrada neste torneio."));
        p.registrarResultado(request.placar1(),request.placar2());
        if(t.getTipo()==TipoTorneio.TABELA)recalcular(t);
        partidas.flush();return t.getPartidas().stream().sorted(Comparator.comparingInt(Partida::getRodada).thenComparingInt(Partida::getOrdem)).map(this::partida).toList();
    }
    @Transactional
    public TorneioResponse finalizar(Long id) {
        Torneio t=autorParaAlterar(id);t.exigirEditavel();
        if(t.getTipo()==TipoTorneio.TABELA)recalcular(t);
        t.finalizar();torneios.flush();return resposta(t);
    }
    private Torneio autorParaAlterar(Long id) {
        Torneio t=torneios.buscarParaAtualizar(id).orElseThrow(()->new RecursoNaoEncontradoException("Torneio não encontrado."));
        SessaoUsuario.exigirAutoria(t);t.atualizarEstado(relogio);return t;
    }
    private Torneio buscar(Long id) {return torneios.findById(id).orElseThrow(()->new RecursoNaoEncontradoException("Torneio não encontrado."));}
    private void exigirFormato(Torneio t,TipoTorneio tipo) {if(t.getTipo()!=tipo)throw new ConflitoException("Operação incompatível com o formato do torneio.");}
    private void criarParticipantes(Torneio t,List<String> nomes) {
        var sorteados=new ArrayList<>(nomes);Collections.shuffle(sorteados);
        for(int i=0;i<sorteados.size();i++)participantes.saveAndFlush(new Participante(t,sorteados.get(i),i+1));
        t.validarParticipantes();
    }
    private void gerar(Torneio t) {
        GeradorCompeticao gerador=t.getTipo()==TipoTorneio.TABELA?new GeradorTabela():new GeradorChaveamento();
        partidas.saveAllAndFlush(gerador.gerar(t));
        if(t.getTipo()==TipoTorneio.TABELA)recalcular(t);
    }
    private void recalcular(Torneio t) {
        classificacoes.saveAll(new ClassificadorTabela().recalcular(t,classificacoes.findByTorneioId(t.getId())));
    }
    private void removerEstrutura(Torneio t,boolean trocarParticipantes,boolean exclusao) {
        var antigasClassificacoes=classificacoes.findByTorneioId(t.getId());
        var antigasPartidas=List.copyOf(t.getPartidas());var antigosParticipantes=List.copyOf(t.getParticipantes());
        if(exclusao)t.desvincularParaExclusao();else t.prepararReconstrucao(trocarParticipantes);
        classificacoes.deleteAll(antigasClassificacoes);classificacoes.flush();
        partidas.deleteAll(antigasPartidas);partidas.flush();
        if(trocarParticipantes){participantes.deleteAll(antigosParticipantes);participantes.flush();}
    }
    private TorneioResponse resposta(Torneio t) {
        return new TorneioResponse(resumo(t),t.getParticipantes().stream().sorted(Comparator.comparingInt(Participante::getOrdemSorteio))
                .map(this::participante).toList());
    }
    private ResumoResponse resumo(Torneio t) {
        return new ResumoResponse(t.getId(),t.getNome(),t.getDescricao(),t.getTipo(),t.estadoEm(relogio),t.getDataInicio(),t.getCriadoEm(),
                new CriadorResponse(t.getCriador().getId(),t.getCriador().getNome()),participante(t.getCampeao()),t.getParticipantes().size());
    }
    private ParticipanteResponse participante(Participante p) {return p==null?null:new ParticipanteResponse(p.getId(),p.getNome(),p.getOrdemSorteio());}
    private PartidaResponse partida(Partida p) {return new PartidaResponse(p.getId(),p.getRodada(),p.getOrdem(),p.getFase(),participante(p.getParticipante1()),
            participante(p.getParticipante2()),p.getPlacar1(),p.getPlacar2(),participante(p.getVencedor()));}
}
