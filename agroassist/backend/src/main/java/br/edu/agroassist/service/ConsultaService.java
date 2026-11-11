package br.edu.agroassist.service;

import br.edu.agroassist.domain.SequenciaProtocolo;
import br.edu.agroassist.dto.Respostas.*;
import br.edu.agroassist.repository.*;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;

/** Consulta agregada para as telas e seletores demonstrativos do protótipo. */
@Service
@Transactional(readOnly = true)
public class ConsultaService {
    private final UsuarioRepository usuarios;
    private final CulturaRepository culturas;
    private final PropriedadeRepository propriedades;
    private final TecnicoRepository tecnicos;
    private final SolicitacaoRepository solicitacoes;
    private final AtendimentoRepository atendimentos;
    private final AvaliacaoRepository avaliacoes;
    private final SequenciaProtocoloRepository sequencias;

    public ConsultaService(
            UsuarioRepository usuarios,
            CulturaRepository culturas,
            PropriedadeRepository propriedades,
            TecnicoRepository tecnicos,
            SolicitacaoRepository solicitacoes,
            AtendimentoRepository atendimentos,
            AvaliacaoRepository avaliacoes,
            SequenciaProtocoloRepository sequencias) {
        this.usuarios = usuarios;
        this.culturas = culturas;
        this.propriedades = propriedades;
        this.tecnicos = tecnicos;
        this.solicitacoes = solicitacoes;
        this.atendimentos = atendimentos;
        this.avaliacoes = avaliacoes;
        this.sequencias = sequencias;
    }

    public BancoResponse carregar() {
        Sort ordem = Sort.by("id");
        return new BancoResponse(
                1,
                Map.of(),
                sequencias.findById(1L).map(SequenciaProtocolo::getUltimo).orElse(0L),
                usuarios.findAll(ordem).stream().map(UsuarioResponse::de).toList(),
                culturas.findAll(ordem).stream().map(CulturaResponse::de).toList(),
                propriedades.findAll(ordem).stream().map(PropriedadeResponse::de).toList(),
                tecnicos.findAll(ordem).stream().map(TecnicoResponse::de).toList(),
                solicitacoes.findAll(ordem).stream().map(SolicitacaoResponse::de).toList(),
                atendimentos.findAll(ordem).stream().map(AtendimentoResponse::de).toList(),
                avaliacoes.findAll(ordem).stream().map(AvaliacaoResponse::de).toList());
    }
}
