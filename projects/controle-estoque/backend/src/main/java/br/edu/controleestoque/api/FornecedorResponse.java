package br.edu.controleestoque.api;

import br.edu.controleestoque.domain.Fornecedor;

public record FornecedorResponse(Long id, String nome, String cnpj, String email, String telefone) {

    public static FornecedorResponse de(Fornecedor f) {
        return new FornecedorResponse(f.getId(), f.getNome(), f.getCnpj(), f.getEmail(), f.getTelefone());
    }
}
