package br.edu.aniva.service;

import br.edu.aniva.domain.Usuario;
import br.edu.aniva.dto.CadastroRequest;
import br.edu.aniva.dto.LoginRequest;
import br.edu.aniva.dto.UsuarioResponse;
import br.edu.aniva.exception.NaoAutenticadoException;
import br.edu.aniva.exception.RegraNegocioException;
import br.edu.aniva.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public UsuarioResponse cadastrar(CadastroRequest request) {
        String email = normalizar(request.email());

        // regra: um e-mail só pode ter uma conta
        if (usuarioRepository.existsByEmail(email)) {
            throw new RegraNegocioException("Este e-mail já está cadastrado.");
        }

        // regra: a senha nunca é guardada em texto puro, somente o hash BCrypt
        var usuario = new Usuario(request.nome(), email, passwordEncoder.encode(request.senha()));
        return UsuarioResponse.de(usuarioRepository.save(usuario));
    }

    @Transactional(readOnly = true)
    public UsuarioResponse login(LoginRequest request) {
        // a mesma mensagem para e-mail inexistente e senha errada, para não revelar quais e-mails existem
        return usuarioRepository.findByEmail(normalizar(request.email()))
                .filter(usuario -> passwordEncoder.matches(request.senha(), usuario.getSenhaHash()))
                .map(UsuarioResponse::de)
                .orElseThrow(() -> new NaoAutenticadoException("E-mail ou senha incorretos."));
    }

    @Transactional(readOnly = true)
    public Optional<UsuarioResponse> buscarPorId(Long id) {
        return usuarioRepository.findById(id).map(UsuarioResponse::de);
    }

    // Usado pelos outros services para obter a pessoa logada.
    @Transactional(readOnly = true)
    public Usuario obterUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NaoAutenticadoException("Sessão expirada. Entre novamente."));
    }

    private String normalizar(String email) {
        return email.trim().toLowerCase();
    }
}
