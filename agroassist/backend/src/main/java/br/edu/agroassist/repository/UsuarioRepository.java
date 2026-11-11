package br.edu.agroassist.repository;

import br.edu.agroassist.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {}
