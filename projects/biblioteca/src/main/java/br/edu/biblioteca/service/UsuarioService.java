package br.edu.biblioteca.service;

import br.edu.biblioteca.domain.Usuario;
import br.edu.biblioteca.exception.RecursoNaoEncontradoException;
import br.edu.biblioteca.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarios;

    public UsuarioService(UsuarioRepository usuarios) {
        this.usuarios = usuarios;
    }

    @Transactional
    public Usuario cadastrar(String nome, String email) {
        Usuario usuario = new Usuario(nome, email);
        if (usuarios.existsByEmail(usuario.getEmail())) {
            throw new IllegalStateException("E-mail já cadastrado: " + usuario.getEmail());
        }
        return usuarios.save(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario buscar(Long id) {
        return usuarios.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + id));
    }
}