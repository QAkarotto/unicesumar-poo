package br.edu.lojajogos.repository;

import br.edu.lojajogos.domain.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // SELECT ... FROM pedido ORDER BY data_pedido DESC
    List<Pedido> findAllByOrderByDataPedidoDesc();
}