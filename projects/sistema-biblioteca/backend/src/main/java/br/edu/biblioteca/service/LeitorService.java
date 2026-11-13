
package br.edu.biblioteca.service;

import br.edu.biblioteca.domain.Leitor;
import br.edu.biblioteca.repository.EmprestimoRepository;
import br.edu.biblioteca.repository.LeitorRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class LeitorService {

    private final LeitorRepository leitorRepository;
    private final EmprestimoRepository emprestimoRepository;

    public LeitorService(
            LeitorRepository leitorRepository,
            EmprestimoRepository emprestimoRepository) {

        this.leitorRepository = leitorRepository;
        this.emprestimoRepository = emprestimoRepository;
    }

    @Transactional
    public Leitor cadastrar(Leitor leitor) {

        validarLeitor(leitor);

        if (leitorRepository.existsByEmail(leitor.getEmail())) {
            throw new IllegalArgumentException(
                "Já existe um leitor cadastrado com este e-mail."
            );
        }

        Leitor novo = new Leitor(
            leitor.getNome(),
            leitor.getEmail(),
            leitor.getTelefone()
        );

        return leitorRepository.save(novo);
    }

    @Transactional(readOnly = true)
    public List<Leitor> listar() {
        return leitorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Leitor buscarPorId(Long id) {
        return leitorRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException(
                "Leitor não encontrado: " + id
            ));
    }

    @Transactional
    public Leitor atualizar(Long id, Leitor dados) {

        validarLeitor(dados);

        Leitor leitor = leitorRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException(
                "Leitor não encontrado: " + id
            ));

        if (!leitor.getEmail().equals(dados.getEmail())
                && leitorRepository.existsByEmail(dados.getEmail())) {

            throw new IllegalArgumentException(
                "Já existe outro leitor com este e-mail."
            );
        }

        leitor.setNome(dados.getNome());
        leitor.setEmail(dados.getEmail());
        leitor.setTelefone(dados.getTelefone());

        return leitorRepository.save(leitor);
    }

    @Transactional
    public void excluir(Long id) {

        Leitor leitor = leitorRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException(
                "Leitor não encontrado: " + id
            ));

        if (emprestimoRepository.existsByLeitorId(id)) {
            throw new IllegalArgumentException(
                "Não é possível excluir um leitor "
                + "que possui histórico de empréstimos."
            );
        }

        leitorRepository.delete(leitor);
    }

    private void validarLeitor(Leitor leitor) {

        if (leitor == null) {
            throw new IllegalArgumentException(
                "Os dados do leitor são obrigatórios."
            );
        }

        if (leitor.getNome() == null
                || leitor.getNome().isBlank()
                || leitor.getNome().length() > 150) {

            throw new IllegalArgumentException(
                "O nome deve ter entre 1 e 150 caracteres."
            );
        }

        if (leitor.getEmail() == null
                || leitor.getEmail().isBlank()
                || leitor.getEmail().length() > 150
                || !leitor.getEmail().matches(
                    "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"
                )) {

            throw new IllegalArgumentException(
                "Informe um e-mail válido de até 150 caracteres."
            );
        }

        if (leitor.getTelefone() != null
                && leitor.getTelefone().length() > 20) {

            throw new IllegalArgumentException(
                "O telefone deve ter no máximo 20 caracteres."
            );
        }
    }
}
