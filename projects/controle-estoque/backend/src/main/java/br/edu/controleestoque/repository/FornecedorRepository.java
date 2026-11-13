package br.edu.controleestoque.repository;

import br.edu.controleestoque.domain.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {

    boolean existsByCnpj(String cnpj);

    boolean existsByCnpjAndIdNot(String cnpj, Long id);

    List<Fornecedor> findAllByOrderByNomeAsc();
}
