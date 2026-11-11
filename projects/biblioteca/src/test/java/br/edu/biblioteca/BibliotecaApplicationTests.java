package br.edu.biblioteca;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled("Depende do MySQL; a validação das regras está em EmprestimoTest")
@SpringBootTest
class BibliotecaApplicationTests {

	@Test
	void contextLoads() {
	}
}