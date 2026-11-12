package br.edu.aniva.service;

import br.edu.aniva.domain.Usuario;
import br.edu.aniva.dto.CadastroRequest;
import br.edu.aniva.dto.LoginRequest;
import br.edu.aniva.exception.NaoAutenticadoException;
import br.edu.aniva.exception.RegraNegocioException;
import br.edu.aniva.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UsuarioServiceTest {

    private UsuarioRepository repository;
    private UsuarioService service;

    @BeforeEach
    void preparar() {
        repository = mock(UsuarioRepository.class);
        service = new UsuarioService(repository);
        when(repository.save(any(Usuario.class))).thenAnswer(chamada -> chamada.getArgument(0));
    }

    @Test
    void naoDevePermitirDoisCadastrosComOMesmoEmail() {
        when(repository.existsByEmail("ana@teste.com")).thenReturn(true);

        assertThrows(RegraNegocioException.class,
                () -> service.cadastrar(new CadastroRequest("Ana", "  ANA@teste.com ", "123456")));

        verify(repository, never()).save(any());
    }

    @Test
    void deveGuardarASenhaComHashENuncaEmTextoPuro() {
        service.cadastrar(new CadastroRequest("Ana", "ana@teste.com", "123456"));

        var captor = ArgumentCaptor.forClass(Usuario.class);
        verify(repository).save(captor.capture());
        assertNotEquals("123456", captor.getValue().getSenhaHash());
        assertEquals("ana@teste.com", captor.getValue().getEmail());
    }

    @Test
    void loginDeveFuncionarComASenhaCertaERecusarASenhaErrada() {
        service.cadastrar(new CadastroRequest("Ana", "ana@teste.com", "123456"));
        var captor = ArgumentCaptor.forClass(Usuario.class);
        verify(repository).save(captor.capture());
        Usuario salvo = captor.getValue();
        when(repository.findByEmail("ana@teste.com")).thenReturn(Optional.of(salvo));

        assertEquals("Ana", service.login(new LoginRequest("ana@teste.com", "123456")).nome());
        assertThrows(NaoAutenticadoException.class,
                () -> service.login(new LoginRequest("ana@teste.com", "errada")));
    }

    @Test
    void loginComEmailInexistenteDeveSerRecusado() {
        when(repository.findByEmail("x@teste.com")).thenReturn(Optional.empty());

        assertThrows(NaoAutenticadoException.class,
                () -> service.login(new LoginRequest("x@teste.com", "123456")));
    }

    @Test
    void obterUsuarioInexistenteDeveExigirNovoLogin() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NaoAutenticadoException.class, () -> service.obterUsuario(99L));
    }
}
