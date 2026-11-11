package br.edu.lojajogos.dto;

import br.edu.lojajogos.domain.Cliente;

public record ClienteResponse(Long id, String nome, String email) {

    public static ClienteResponse de(Cliente cliente) {
        return new ClienteResponse(cliente.getId(), cliente.getNome(), cliente.getEmail());
    }
}