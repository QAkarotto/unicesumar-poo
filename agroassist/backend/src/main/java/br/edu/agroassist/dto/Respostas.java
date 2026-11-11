package br.edu.agroassist.dto;

import br.edu.agroassist.domain.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/** DTOs mantêm o contrato do React sem expor entidades ou proxies do Hibernate. */
public final class Respostas {
    private Respostas() {}

    public record UsuarioResponse(
            String id,
            String nome,
            String perfil,
            String telefone,
            String email,
            String municipio,
            LocalDateTime consentimentoEm) {
        public static UsuarioResponse de(Usuario u) {
            return new UsuarioResponse(
                    u.getId(),
                    u.getNome(),
                    u.getPerfil().getValor(),
                    u.getTelefone(),
                    u.getEmail(),
                    u.getMunicipio(),
                    u.getConsentimentoEm());
        }
    }

    public record CulturaResponse(String id, String nome) {
        public static CulturaResponse de(Cultura c) {
            return new CulturaResponse(c.getId(), c.getNome());
        }
    }

    public record PropriedadeResponse(
            String id,
            String produtorId,
            String nome,
            String municipio,
            BigDecimal areaHa,
            List<String> culturaIds) {
        public static PropriedadeResponse de(Propriedade p) {
            return new PropriedadeResponse(
                    p.getId(),
                    p.getProdutor().getId(),
                    p.getNome(),
                    p.getMunicipio(),
                    p.getAreaHa(),
                    p.getCulturas().stream().map(Cultura::getId).sorted().toList());
        }
    }

    public record TecnicoResponse(
            String id,
            String usuarioId,
            String formacao,
            String registro,
            List<String> culturaIds,
            boolean ativo) {
        public static TecnicoResponse de(Tecnico t) {
            return new TecnicoResponse(
                    t.getId(),
                    t.getUsuario().getId(),
                    t.getFormacao(),
                    t.getRegistro(),
                    t.getCulturas().stream().map(Cultura::getId).sorted().toList(),
                    t.isAtivo());
        }
    }

    public record HistoricoResponse(LocalDateTime em, String status, String autorId, String descricao) {
        static HistoricoResponse de(EventoHistorico e) {
            return new HistoricoResponse(
                    e.getEm(), e.getStatus().getDescricao(), e.getAutor().getId(), e.getDescricao());
        }
    }

    public record SolicitacaoResponse(
            String id,
            String protocolo,
            String produtorId,
            String propriedadeId,
            String culturaId,
            String tecnicoId,
            String estadio,
            String descricao,
            List<String> fotos,
            String status,
            LocalDateTime criadaEm,
            List<HistoricoResponse> historico) {
        public static SolicitacaoResponse de(Solicitacao s) {
            return new SolicitacaoResponse(
                    s.getId(),
                    s.getProtocolo(),
                    s.getProdutor().getId(),
                    s.getPropriedade().getId(),
                    s.getCultura().getId(),
                    s.getTecnico() == null ? null : s.getTecnico().getId(),
                    s.getEstadio(),
                    s.getDescricao(),
                    s.getFotos(),
                    s.getStatus().getDescricao(),
                    s.getCriadaEm(),
                    s.getHistorico().stream().map(HistoricoResponse::de).toList());
        }
    }

    public record AtendimentoResponse(
            String id,
            String solicitacaoId,
            String tecnicoId,
            LocalDate data,
            String modalidade,
            String diagnostico,
            String recomendacao) {
        public static AtendimentoResponse de(Atendimento a) {
            return new AtendimentoResponse(
                    a.getId(),
                    a.getSolicitacao().getId(),
                    a.getTecnico().getId(),
                    a.getData(),
                    a.getModalidade().getDescricao(),
                    a.getDiagnostico(),
                    a.getRecomendacao());
        }
    }

    public record AvaliacaoResponse(
            String id, String solicitacaoId, int nota, String comentario, LocalDateTime em) {
        public static AvaliacaoResponse de(Avaliacao a) {
            return new AvaliacaoResponse(
                    a.getId(), a.getSolicitacao().getId(), a.getNota(), a.getComentario(), a.getEm());
        }
    }

    public record BancoResponse(
            int versao,
            Map<String, Integer> sequencias,
            long ultimoProtocolo,
            List<UsuarioResponse> usuarios,
            List<CulturaResponse> culturas,
            List<PropriedadeResponse> propriedades,
            List<TecnicoResponse> tecnicos,
            List<SolicitacaoResponse> solicitacoes,
            List<AtendimentoResponse> atendimentos,
            List<AvaliacaoResponse> avaliacoes) {}
}
