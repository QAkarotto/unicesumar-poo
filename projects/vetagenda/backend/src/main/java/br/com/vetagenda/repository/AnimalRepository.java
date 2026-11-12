package br.com.vetagenda.repository;
import br.com.vetagenda.domain.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AnimalRepository extends JpaRepository<Animal, Long> {}