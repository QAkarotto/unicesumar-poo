package br.edu.estoque.service;

import br.edu.estoque.domain.*;
import br.edu.estoque.dto.ProdutoRequest;
import br.edu.estoque.exception.*;
import br.edu.estoque.repository.*;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoService {
    private final ProdutoRepository produtos;
    private final CategoriaRepository categorias;

    public ProdutoService(ProdutoRepository produtos, CategoriaRepository categorias) {
        this.produtos = produtos; this.categorias = categorias;
    }

    public Produto criar(ProdutoRequest r) {
        var sku = r.sku().trim();
        if (produtos.existsBySku(sku))
            throw new RegraNegocioException("sku", "Já existe um produto com o SKU \"" + sku + "\".");
        return produtos.save(new Produto(sku, r.nome().trim(), r.preco(), r.estoqueMinimo(), categoria(r.categoriaId())));
    }

    @Transactional
    public Produto atualizar(Long id, ProdutoRequest r) {
        var p = buscar(id);
        p.atualizar(r.nome(), r.preco(), r.estoqueMinimo(), categoria(r.categoriaId()));
        return p;
    }

    @Transactional
    public Produto desativar(Long id) { var p = buscar(id); p.desativar(); return p; }

    public Produto buscar(Long id) {
        return produtos.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado."));
    }

    public List<Produto> listar() { return produtos.findAll(); }
    public List<Produto> abaixoDoMinimo() { return produtos.findAbaixoDoMinimo(); }

    private Categoria categoria(Long id) {
        return categorias.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada."));
    }
}
