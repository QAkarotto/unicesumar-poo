package br.edu.lojajogos.repository;

import br.edu.lojajogos.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // O Spring Data monta o SQL a partir do nome do método:
    // SELECT COUNT(*) > 0 FROM cliente WHERE email = ?
    boolean existsByEmail(String email);
}