package br.edu.aniva.service;

import br.edu.aniva.domain.Obra;
import br.edu.aniva.dto.ObraResponse;
import br.edu.aniva.exception.RecursoNaoEncontradoException;
import br.edu.aniva.repository.ObraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ObraService {

    private final ObraRepository obraRepository;

    public ObraService(ObraRepository obraRepository) {
        this.obraRepository = obraRepository;
    }

    @Transactional(readOnly = true)
    public List<ObraResponse> listar() {
        return obraRepository.findAll().stream().map(ObraResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public ObraResponse buscar(String id) {
        return ObraResponse.de(obterObra(id));
    }

    // Usado pelos outros services para garantir que a obra existe (404 caso contrário).
    @Transactional(readOnly = true)
    public Obra obterObra(String id) {
        return obraRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Obra não encontrada."));
    }
}
