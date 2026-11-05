package br.edu.sistemaacademico.service;

import br.edu.sistemaacademico.dto.OfertaResponse;
import br.edu.sistemaacademico.repository.OfertaDisciplinaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OfertaService {

    private final OfertaDisciplinaRepository ofertaRepository;

    public OfertaService(OfertaDisciplinaRepository ofertaRepository) {
        this.ofertaRepository = ofertaRepository;
    }

    public List<OfertaResponse> listar() {
        return ofertaRepository.findAll().stream().map(OfertaResponse::de).toList();
    }
}
