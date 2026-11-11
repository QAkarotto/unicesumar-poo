package br.edu.biblioteca.service;

import br.edu.biblioteca.domain.Leitor;
import br.edu.biblioteca.dto.*;
import br.edu.biblioteca.exception.*;
import br.edu.biblioteca.repository.*;
import java.util.*;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LeitorService {
    private final LeitorRepository leitores;
    private final EmprestimoRepository emprestimos;
    public LeitorService(LeitorRepository leitores, EmprestimoRepository emprestimos) {
        this.leitores = leitores; this.emprestimos = emprestimos;
    }
    public List<LeitorResponse> listar() { return leitores.findAll(Sort.by("nome")).stream().map(this::resposta).toList(); }
    public LeitorResponse buscar(Long id) {
        return resposta(leitores.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Leitor não encontrado.")));
    }
    @Transactional
    public LeitorResponse criar(LeitorRequest r) {
        String email = r.email().strip().toLowerCase(Locale.ROOT);
        if (leitores.existsByEmail(email)) throw new RegraNegocioException("Já existe um leitor com este e-mail.");
        return resposta(leitores.save(new Leitor(r.nome(), email)));
    }
    @Transactional
    public LeitorResponse atualizar(Long id, LeitorRequest r) {
        var leitor = bloqueado(id);
        String email = r.email().strip().toLowerCase(Locale.ROOT);
        if (leitores.existsByEmailAndIdNot(email, id)) throw new RegraNegocioException("Já existe um leitor com este e-mail.");
        leitor.atualizar(r.nome(), email);
        return resposta(leitor);
    }
    @Transactional
    public LeitorResponse alterarAtividade(Long id, boolean ativo) {
        var leitor = bloqueado(id);
        leitor.alterarAtividade(ativo, emprestimos.countByLeitorIdAndDevolvidoEmIsNull(id));
        return resposta(leitor);
    }
    private Leitor bloqueado(Long id) {
        return leitores.buscarComBloqueio(id).orElseThrow(() -> new RecursoNaoEncontradoException("Leitor não encontrado."));
    }
    private LeitorResponse resposta(Leitor l) { return LeitorResponse.de(l, emprestimos.countByLeitorIdAndDevolvidoEmIsNull(l.getId())); }
}
