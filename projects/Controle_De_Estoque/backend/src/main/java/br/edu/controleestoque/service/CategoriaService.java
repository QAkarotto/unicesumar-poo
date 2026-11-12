package br.edu.controleestoque.service;

import br.edu.controleestoque.domain.AcaoLog;
import br.edu.controleestoque.domain.Categoria;
import br.edu.controleestoque.domain.EntidadeLog;
import br.edu.controleestoque.dto.CategoriaRequest;
import br.edu.controleestoque.dto.CategoriaResponse;
import br.edu.controleestoque.exception.RecursoNaoEncontradoException;
import br.edu.controleestoque.repository.CategoriaRepository;
import br.edu.controleestoque.repository.ProdutoRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;
    private final LogService logService;

    public CategoriaService(CategoriaRepository categoriaRepository,
                            ProdutoRepository produtoRepository,
                            LogService logService) {
        this.categoriaRepository = categoriaRepository;
        this.produtoRepository = produtoRepository;
        this.logService = logService;
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listar() {
        return categoriaRepository.findAll().stream()
                .map(CategoriaResponse::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponse buscar(Long id) {
        return CategoriaResponse.de(buscarEntidade(id));
    }

    @Transactional
    public CategoriaResponse criar(CategoriaRequest request) {
        if (categoriaRepository.existsByNomeIgnoreCase(request.nome().trim())) {
            throw new IllegalStateException("Já existe uma categoria com o nome informado.");
        }

        var categoria = categoriaRepository.save(new Categoria(request.nome()));
        logService.registrar(AcaoLog.CRIAR, EntidadeLog.CATEGORIA, categoria.getId(),
                "Categoria criada: " + categoria.getNome());
        return CategoriaResponse.de(categoria);
    }

    @Transactional
    public CategoriaResponse atualizar(Long id, CategoriaRequest request) {
        var categoria = buscarEntidade(id);
        if (categoriaRepository.existsByNomeIgnoreCaseAndIdNot(request.nome().trim(), id)) {
            throw new IllegalStateException("Já existe uma categoria com o nome informado.");
        }

        var nomeAnterior = categoria.getNome();
        categoria.renomear(request.nome());
        logService.registrar(AcaoLog.ATUALIZAR, EntidadeLog.CATEGORIA, id,
                "Categoria renomeada de " + nomeAnterior + " para " + categoria.getNome());
        return CategoriaResponse.de(categoria);
    }

    @Transactional
    public void excluir(Long id) {
        var categoria = buscarEntidade(id);
        if (produtoRepository.existsByCategoriaId(id)) {
            throw new IllegalStateException("Não é possível excluir uma categoria que possui produtos.");
        }

        categoriaRepository.delete(categoria);
        logService.registrar(AcaoLog.EXCLUIR, EntidadeLog.CATEGORIA, id,
                "Categoria excluída: " + categoria.getNome());
    }

    private Categoria buscarEntidade(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada."));
    }
}
