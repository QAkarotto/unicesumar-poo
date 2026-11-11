package br.edu.agroassist.service;

import br.edu.agroassist.domain.*;
import br.edu.agroassist.dto.Requisicoes.*;
import br.edu.agroassist.dto.Respostas.*;
import br.edu.agroassist.exception.*;
import br.edu.agroassist.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class CadastroService {
    private final UsuarioRepository usuarios;
    private final CulturaRepository culturas;
    private final PropriedadeRepository propriedades;
    private final TecnicoRepository tecnicos;

    public CadastroService(
            UsuarioRepository usuarios,
            CulturaRepository culturas,
            PropriedadeRepository propriedades,
            TecnicoRepository tecnicos) {
        this.usuarios = usuarios;
        this.culturas = culturas;
        this.propriedades = propriedades;
        this.tecnicos = tecnicos;
    }

    public UsuarioResponse criarProdutor(DadosConta dados) {
        Usuario usuario =
                Usuario.criarProdutor(
                        dados.nome(),
                        dados.telefone(),
                        dados.email(),
                        dados.municipio(),
                        dados.consentimento());
        return UsuarioResponse.de(usuarios.save(usuario));
    }

    public PropriedadeResponse criarPropriedade(String autorId, DadosPropriedade dados) {
        Usuario autor = buscarUsuario(autorId);
        Propriedade propriedade =
                new Propriedade(
                        "p" + UUID.randomUUID(),
                        autor,
                        dados.nome(),
                        dados.municipio(),
                        dados.areaHa(),
                        buscarCulturas(dados.culturaIds()));
        return PropriedadeResponse.de(propriedades.save(propriedade));
    }

    public TecnicoResponse criarTecnico(String autorId, DadosTecnico dados) {
        buscarUsuario(autorId).exigirPerfil(Perfil.ADMIN);
        if (tecnicos.existsByRegistroIgnoreCase(dados.registro().trim()))
            throw new RegraNegocioException(
                    "REGISTRO_DUPLICADO", "Já existe um técnico com este registro profissional.");
        Usuario usuario =
                new Usuario(
                        "u" + UUID.randomUUID(),
                        dados.nome(),
                        Perfil.TECNICO,
                        dados.telefone(),
                        "",
                        dados.municipio(),
                        LocalDateTime.now());
        Tecnico tecnico =
                new Tecnico(
                        "t" + UUID.randomUUID(),
                        usuario,
                        dados.formacao(),
                        dados.registro(),
                        buscarCulturas(dados.culturaIds()));
        usuarios.save(usuario);
        return TecnicoResponse.de(tecnicos.save(tecnico));
    }

    public TecnicoResponse alterarSituacao(String autorId, String tecnicoId, boolean ativo) {
        buscarUsuario(autorId).exigirPerfil(Perfil.ADMIN);
        Tecnico tecnico =
                tecnicos.buscarParaAlterar(tecnicoId)
                        .orElseThrow(() -> new RecursoNaoEncontradoException("Técnico não encontrado."));
        tecnico.alterarSituacao(ativo);
        return TecnicoResponse.de(tecnico);
    }

    @Transactional(readOnly = true)
    public Usuario buscarUsuario(String id) {
        return usuarios.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado."));
    }

    private Set<Cultura> buscarCulturas(List<String> ids) {
        Set<Cultura> encontradas = new LinkedHashSet<>();
        for (String id : ids) {
            encontradas.add(
                    culturas.findById(id)
                            .orElseThrow(() ->
                                    new RecursoNaoEncontradoException("Cultura não encontrada: " + id)));
        }
        return encontradas;
    }
}
