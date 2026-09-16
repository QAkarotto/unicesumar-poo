import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SemestreTest {

    @Test
    @DisplayName("Deve conter exatamente os valores PRIMEIRO e SEGUNDO")
    void deveConterTodosOsValores() {
        var semestres = Semestre.values();

        assertEquals(2, semestres.length);
        assertEquals(Semestre.PRIMEIRO, Semestre.valueOf("PRIMEIRO"));
        assertEquals(Semestre.SEGUNDO, Semestre.valueOf("SEGUNDO"));
    }
}
