package br.edu.estoque.service;

import br.edu.estoque.domain.Categoria;
import br.edu.estoque.exception.*;
import br.edu.estoque.repository.*;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {
    private final CategoriaRepository categorias;
    private final ProdutoRepository produtos;

    public CategoriaService(CategoriaRepository categorias, ProdutoRepository produtos) {
        this.categorias = categorias; this.produtos = produtos;
    }

    public Categoria criar(String nome) {
        var n = nome.trim();
        if (categorias.existsByNomeIgnoreCase(n))
            throw new RegraNegocioException("nome", "Já existe uma categoria com o nome \"" + n + "\".");
        return categorias.save(new Categoria(n));
    }
    public List<Categoria> listar() { return categorias.findAll(); }

    public void excluir(Long id) {
        if (!categorias.existsById(id)) throw new RecursoNaoEncontradoException("Categoria não encontrada.");
        if (produtos.existsByCategoriaId(id))
            throw new RegraNegocioException("Não é possível excluir categoria que possui produtos.");
        categorias.deleteById(id);
    }
}
