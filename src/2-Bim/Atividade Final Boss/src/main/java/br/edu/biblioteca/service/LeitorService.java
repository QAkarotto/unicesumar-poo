package br.edu.biblioteca.service;

import br.edu.biblioteca.domain.Leitor;
import br.edu.biblioteca.repository.LeitorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LeitorService {

    private final LeitorRepository leitorRepository;

    public LeitorService(LeitorRepository leitorRepository) {
        this.leitorRepository = leitorRepository;
    }

    @Transactional
    public Leitor cadastrar(String nome, String email) {
        email = email.trim().toLowerCase();
        if (leitorRepository.existsByEmail(email)) {
            throw new IllegalStateException("Já existe um leitor com este e-mail.");
        }
        return leitorRepository.save(new Leitor(nome.trim(), email));
    }

    public List<Leitor> listar() {
        return leitorRepository.findAll();
    }

    public Leitor buscarPorId(Long id) {
        return leitorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Leitor não encontrado: " + id));
    }

    @Transactional
    public Leitor atualizar(Long id, String nome, String email) {
        Leitor leitor = buscarPorId(id);
        email = email.trim().toLowerCase();

        Leitor outro = leitorRepository.findByEmail(email).orElse(null);
        if (outro != null && !outro.getId().equals(id)) {
            throw new IllegalStateException("Já existe um leitor com este e-mail.");
        }

        leitor.atualizar(nome.trim(), email);
        return leitor;
    }

    @Transactional
    public void excluir(Long id) {
        leitorRepository.delete(buscarPorId(id));
    }
}
