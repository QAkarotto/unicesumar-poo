package br.com.vetagenda.service;

import br.com.vetagenda.domain.Veterinario;
import br.com.vetagenda.dto.requests.VeterinarioRequest;
import br.com.vetagenda.dto.responses.VeterinarioResponse;
import br.com.vetagenda.exception.RecursoNaoEncontradoException;
import br.com.vetagenda.repository.EspecialidadeRepository;
import br.com.vetagenda.repository.VeterinarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class VeterinarioService {
    private final VeterinarioRepository veterinarios;
    private final EspecialidadeRepository especialidades;
    public VeterinarioService(VeterinarioRepository veterinarios, EspecialidadeRepository especialidades) {
        this.veterinarios = veterinarios; this.especialidades = especialidades;
    }
    @Transactional
    public VeterinarioResponse cadastrar(VeterinarioRequest req) {
        var especialidade = especialidades.findById(req.especialidadeId())
            .orElseThrow(() -> new RecursoNaoEncontradoException("Especialidade não encontrada."));
        return VeterinarioResponse.from(veterinarios.save(
            new Veterinario(req.nome(), req.crmv(), req.telefone(), especialidade)));
    }
    @Transactional(readOnly = true)
    public List<VeterinarioResponse> listar() {
        return veterinarios.findAll().stream().map(VeterinarioResponse::from).toList();
    }
}
