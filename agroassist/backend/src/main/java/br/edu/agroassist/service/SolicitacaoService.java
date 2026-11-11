package br.edu.agroassist.service;

import br.edu.agroassist.domain.*;
import br.edu.agroassist.dto.Requisicoes.*;
import br.edu.agroassist.dto.Respostas.*;
import br.edu.agroassist.exception.RecursoNaoEncontradoException;
import br.edu.agroassist.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class SolicitacaoService {
    private final CadastroService cadastros;
    private final SolicitacaoRepository solicitacoes;
    private final PropriedadeRepository propriedades;
    private final CulturaRepository culturas;
    private final TecnicoRepository tecnicos;
    private final AtendimentoRepository atendimentos;
    private final AvaliacaoRepository avaliacoes;
    private final SequenciaProtocoloRepository sequencias;

    public SolicitacaoService(
            CadastroService cadastros,
            SolicitacaoRepository solicitacoes,
            PropriedadeRepository propriedades,
            CulturaRepository culturas,
            TecnicoRepository tecnicos,
            AtendimentoRepository atendimentos,
            AvaliacaoRepository avaliacoes,
            SequenciaProtocoloRepository sequencias) {
        this.cadastros = cadastros;
        this.solicitacoes = solicitacoes;
        this.propriedades = propriedades;
        this.culturas = culturas;
        this.tecnicos = tecnicos;
        this.atendimentos = atendimentos;
        this.avaliacoes = avaliacoes;
        this.sequencias = sequencias;
    }

    public SolicitacaoResponse abrir(String autorId, DadosSolicitacao dados) {
        Usuario autor = cadastros.buscarUsuario(autorId);
        Propriedade propriedade =
                propriedades
                        .findById(dados.propriedadeId())
                        .orElseThrow(() -> new RecursoNaoEncontradoException("Propriedade não encontrada."));
        Cultura cultura =
                culturas.findById(dados.culturaId())
                        .orElseThrow(() -> new RecursoNaoEncontradoException("Cultura não encontrada."));
        SequenciaProtocolo sequencia =
                sequencias
                        .buscarParaIncrementar()
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Execute database/schema.sql para criar o contador de protocolos."));
        LocalDateTime em = LocalDateTime.now();
        String protocolo = "AGA-" + em.getYear() + "-" + String.format("%04d", sequencia.proximo());
        Solicitacao solicitacao =
                new Solicitacao(
                        "s" + UUID.randomUUID(),
                        protocolo,
                        autor,
                        propriedade,
                        cultura,
                        dados.estadio(),
                        dados.descricao(),
                        dados.fotos(),
                        em);
        return SolicitacaoResponse.de(solicitacoes.save(solicitacao));
    }

    @Transactional(readOnly = true)
    public SolicitacaoResponse buscar(String id) {
        return SolicitacaoResponse.de(
                solicitacoes
                        .findById(id)
                        .orElseThrow(() -> new RecursoNaoEncontradoException("Solicitação não encontrada.")));
    }

    public SolicitacaoResponse atribuir(String autorId, String id, String tecnicoId) {
        Usuario autor = cadastros.buscarUsuario(autorId);
        Solicitacao solicitacao = buscarParaAlterar(id);
        Tecnico tecnico =
                tecnicos.buscarParaAlterar(tecnicoId)
                        .orElseThrow(() -> new RecursoNaoEncontradoException("Técnico não encontrado."));
        solicitacao.atribuir(tecnico, autor, LocalDateTime.now());
        return SolicitacaoResponse.de(solicitacao);
    }

    public SolicitacaoResponse iniciar(String autorId, String id) {
        Solicitacao solicitacao = buscarParaAlterar(id);
        solicitacao.iniciar(cadastros.buscarUsuario(autorId), LocalDateTime.now());
        return SolicitacaoResponse.de(solicitacao);
    }

    public AtendimentoResponse concluir(String autorId, String id, DadosAtendimento dados) {
        Solicitacao solicitacao = buscarParaAlterar(id);
        Atendimento atendimento =
                solicitacao.concluir(
                        cadastros.buscarUsuario(autorId),
                        dados.data(),
                        Modalidade.deDescricao(dados.modalidade()),
                        dados.diagnostico(),
                        dados.recomendacao(),
                        LocalDateTime.now());
        return AtendimentoResponse.de(atendimentos.save(atendimento));
    }

    public AvaliacaoResponse avaliar(String autorId, String id, DadosAvaliacao dados) {
        Solicitacao solicitacao = buscarParaAlterar(id);
        Avaliacao avaliacao =
                solicitacao.avaliar(
                        cadastros.buscarUsuario(autorId),
                        dados.nota(),
                        dados.comentario(),
                        LocalDateTime.now());
        return AvaliacaoResponse.de(avaliacoes.save(avaliacao));
    }

    private Solicitacao buscarParaAlterar(String id) {
        return solicitacoes
                .buscarParaAlterar(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Solicitação não encontrada."));
    }
}
