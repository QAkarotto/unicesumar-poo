package br.edu.seugame.service;

import br.edu.seugame.domain.*;
import br.edu.seugame.dto.ContaDtos.*;
import br.edu.seugame.exception.*;
import br.edu.seugame.repository.*;
import br.edu.seugame.security.SessaoUsuario;
import java.nio.charset.StandardCharsets;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Profile("!infra")
public class ContaService {
    private final UsuarioRepository usuarios;
    private final TorneioRepository torneios;
    private final PasswordEncoder encoder;
    private final java.time.Clock relogio;
    public ContaService(UsuarioRepository usuarios,TorneioRepository torneios,PasswordEncoder encoder,java.time.Clock relogio) {
        this.usuarios=usuarios;this.torneios=torneios;this.encoder=encoder;this.relogio=relogio;
    }
    @Transactional
    public UsuarioResponse cadastrar(CadastroRequest request) {
        int bytes=request.senha().getBytes(StandardCharsets.UTF_8).length;
        if(bytes<8) throw new IllegalArgumentException("A senha deve ter pelo menos 8 caracteres.");
        if(bytes>72) throw new IllegalArgumentException("A senha é muito longa. Use uma senha mais curta.");
        if(!request.senha().equals(request.confirmarSenha()))
            throw new IllegalArgumentException("As senhas não coincidem.");
        if(usuarios.findByUsuario(request.usuario().trim()).isPresent() || usuarios.findByEmail(request.email().trim()).isPresent())
            throw new ConflitoException("Usuário ou e-mail já cadastrado.");
        var u=new Usuario(request.nome(),request.usuario(),request.email(),encoder.encode(request.senha()));
        return resposta(usuarios.saveAndFlush(u));
    }
    @Transactional(readOnly=true)
    public UsuarioResponse perfil() { return resposta(atual()); }
    @Transactional(readOnly=true)
    public java.util.List<MeuTorneioResponse> meusTorneios() {
        return torneios.findByCriadorIdOrderByCriadoEmDesc(SessaoUsuario.id()).stream()
                .map(t -> new MeuTorneioResponse(t.getId(),t.getNome(),t.getTipo(),t.estadoEm(relogio),t.getDataInicio(),
                        t.getParticipantes().size(),t.getPartidas().stream().filter(Partida::temResultado).count(),
                        t.getCampeao()==null?null:new br.edu.seugame.dto.TorneioDtos.ParticipanteResponse(
                                t.getCampeao().getId(),t.getCampeao().getNome(),t.getCampeao().getOrdemSorteio())))
                .toList();
    }
    @Transactional
    public UsuarioResponse alterarPerfil(PerfilRequest request) {
        var u=atual();
        usuarios.findByEmail(request.email().trim()).filter(outro -> !outro.getId().equals(u.getId()))
                .ifPresent(outro -> {throw new ConflitoException("E-mail já cadastrado.");});
        u.alterarPerfil(request.nome(),request.email()); usuarios.flush(); return resposta(u);
    }
    private Usuario atual() { return usuarios.findById(SessaoUsuario.id()).orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado.")); }
    private UsuarioResponse resposta(Usuario u) {
        return new UsuarioResponse(u.getId(),u.getNome(),u.getUsuario(),u.getEmail(),
                torneios.countByCriadorId(u.getId()),torneios.countByCriadorIdAndStatus(u.getId(),StatusTorneio.FINALIZADO));
    }
}
