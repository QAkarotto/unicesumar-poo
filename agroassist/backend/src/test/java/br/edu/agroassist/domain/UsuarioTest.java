package br.edu.agroassist.domain;

import br.edu.agroassist.exception.RegraNegocioException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void registraConsentimentoAoCriarContaDoProdutor() {
        Usuario produtor =
                Usuario.criarProdutor("Ana Silva", "(42) 99999-0000", "ana@exemplo.com", "Castro", true);

        assertEquals(Perfil.PRODUTOR, produtor.getPerfil());
        assertNotNull(produtor.getConsentimentoEm());
        assertNotNull(produtor.getId());
    }

    @Test
    void naoCriaContaSemConsentimento() {
        assertThrows(
                RegraNegocioException.class,
                () ->
                        Usuario.criarProdutor(
                                "Ana Silva", "(42) 99999-0000", "ana@exemplo.com", "Castro", false));
    }
}
