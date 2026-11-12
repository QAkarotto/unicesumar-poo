package br.com.vetagenda.repository;
import br.com.vetagenda.domain.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;
public interface VeterinarioRepository extends JpaRepository<Veterinario, Long> {}