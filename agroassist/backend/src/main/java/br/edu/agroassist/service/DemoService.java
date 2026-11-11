package br.edu.agroassist.service;

import br.edu.agroassist.domain.Perfil;
import br.edu.agroassist.exception.OperacaoNaoPermitidaException;
import br.edu.agroassist.repository.*;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.sql.DataSource;

@Service
public class DemoService {
    private final boolean habilitado;
    private final DataSource dataSource;
    private final EntityManager entityManager;
    private final CadastroService cadastros;
    private final UsuarioRepository usuarios;
    private final CulturaRepository culturas;
    private final PropriedadeRepository propriedades;
    private final TecnicoRepository tecnicos;
    private final SolicitacaoRepository solicitacoes;
    private final AtendimentoRepository atendimentos;
    private final AvaliacaoRepository avaliacoes;

    public DemoService(
            @Value("${agroassist.demo.enabled:true}") boolean habilitado,
            DataSource dataSource,
            EntityManager entityManager,
            CadastroService cadastros,
            UsuarioRepository usuarios,
            CulturaRepository culturas,
            PropriedadeRepository propriedades,
            TecnicoRepository tecnicos,
            SolicitacaoRepository solicitacoes,
            AtendimentoRepository atendimentos,
            AvaliacaoRepository avaliacoes) {
        this.habilitado = habilitado;
        this.dataSource = dataSource;
        this.entityManager = entityManager;
        this.cadastros = cadastros;
        this.usuarios = usuarios;
        this.culturas = culturas;
        this.propriedades = propriedades;
        this.tecnicos = tecnicos;
        this.solicitacoes = solicitacoes;
        this.atendimentos = atendimentos;
        this.avaliacoes = avaliacoes;
    }

    @Transactional
    public void inicializar() {
        if (habilitado && usuarios.count() == 0) carregarExemplos();
    }

    @Transactional
    public void reiniciar(String autorId) {
        if (!habilitado)
            throw new OperacaoNaoPermitidaException("A restauração dos dados de exemplo está desabilitada.");
        cadastros.buscarUsuario(autorId).exigirPerfil(Perfil.ADMIN);
        // Ordem inversa das dependências. deleteAll remove também as coleções JPA.
        avaliacoes.deleteAllInBatch();
        atendimentos.deleteAllInBatch();
        solicitacoes.deleteAll();
        solicitacoes.flush();
        propriedades.deleteAll();
        tecnicos.deleteAll();
        propriedades.flush();
        culturas.deleteAllInBatch();
        usuarios.deleteAllInBatch();
        entityManager.clear();
        carregarExemplos();
    }

    private void carregarExemplos() {
        ResourceDatabasePopulator script = new ResourceDatabasePopulator(new ClassPathResource("seed.sql"));
        script.setSqlScriptEncoding("UTF-8");
        script.execute(dataSource);
    }
}
