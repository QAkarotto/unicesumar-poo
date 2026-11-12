package br.edu.seugame.config;

import br.edu.seugame.repository.UsuarioRepository;
import br.edu.seugame.security.UsuarioAutenticado;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class CredenciaisConfiguration {
    @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(12); }

    @Bean @Profile("!infra")
    UserDetailsService usuariosAutenticacao(UsuarioRepository usuarios) {
        return identificador -> {
            String valor = identificador.trim();
            var usuario = usuarios.findByUsuario(valor).or(() -> usuarios.findByEmail(valor))
                    .orElseThrow(() -> new UsernameNotFoundException("Credenciais inválidas."));
            return new UsuarioAutenticado(usuario);
        };
    }
    @Bean @Profile("infra")
    UserDetailsService semUsuariosNoPerfilInfra() {
        return identificador -> { throw new UsernameNotFoundException("Perfil sem autenticação de contas."); };
    }
    @Bean
    AuthenticationManager authenticationManager(UserDetailsService usuarios, PasswordEncoder encoder) {
        var provider = new DaoAuthenticationProvider(usuarios);
        provider.setPasswordEncoder(encoder);
        return new ProviderManager(provider);
    }
}
