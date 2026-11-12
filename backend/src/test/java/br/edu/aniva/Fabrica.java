package br.edu.aniva;

import br.edu.aniva.domain.Obra;
import br.edu.aniva.domain.Usuario;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/** Cria entidades para os testes, definindo o id (que normalmente vem do banco). */
public final class Fabrica {

    private Fabrica() {
    }

    public static Usuario usuario(Long id, String nome) {
        Usuario usuario = new Usuario(nome, nome.toLowerCase() + "@teste.com", "hash");
        definir(usuario, "id", id);
        return usuario;
    }

    public static Obra obra(String id) {
        try {
            Constructor<Obra> construtor = Obra.class.getDeclaredConstructor();
            construtor.setAccessible(true);
            Obra obra = construtor.newInstance();
            definir(obra, "id", id);
            return obra;
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void definir(Object alvo, String campo, Object valor) {
        try {
            Field field = alvo.getClass().getDeclaredField(campo);
            field.setAccessible(true);
            field.set(alvo, valor);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }
}
