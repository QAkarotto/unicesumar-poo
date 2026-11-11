package br.edu.biblioteca;

import br.edu.biblioteca.domain.*;
import br.edu.biblioteca.dto.*;
import br.edu.biblioteca.exception.RegraNegocioException;
import br.edu.biblioteca.repository.*;
import br.edu.biblioteca.service.*;
import java.net.URI;
import java.net.http.*;
import java.time.*;
import java.util.List;
import java.util.concurrent.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class IntegracaoTest {
    @LocalServerPort int port;
    @Autowired LivroRepository livros;
    @Autowired LeitorRepository leitores;
    @Autowired EmprestimoRepository emprestimos;
    @Autowired EmprestimoService service;
    @Autowired LivroService livroService;
    @Autowired LeitorService leitorService;
    @Autowired Clock clock;
    private final HttpClient http = HttpClient.newHttpClient();

    @BeforeEach void limpar() { emprestimos.deleteAll(); livros.deleteAll(); leitores.deleteAll(); }

    private Livro livro(String isbn, int quantidade) { return livros.save(new Livro("Livro de teste", "Autor", isbn, "Literatura", quantidade)); }
    private Leitor leitor(String email) { return leitores.save(new Leitor("Leitor de teste", email)); }
    private HttpResponse<String> chamar(String metodo, String path, String json) throws Exception {
        var request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/api" + path))
            .timeout(Duration.ofSeconds(15)).header("Content-Type", "application/json")
            .method(metodo, json == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(json)).build();
        return http.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test void fluxoHttpCompletoComPersistenciaEDevolucao() throws Exception {
        var livroCriado = chamar("POST", "/livros", """
            {"titulo":"Dom Casmurro","autor":"Machado de Assis","isbn":"9788535914849","categoria":"Romance","exemplares":1}
            """);
        assertEquals(201, livroCriado.statusCode());
        assertTrue(livroCriado.headers().firstValue("Location").orElseThrow().startsWith("/api/livros/"));
        assertEquals(201, chamar("POST", "/leitores", "{\"nome\":\"Ana\",\"email\":\"ana@exemplo.com\"}").statusCode());
        Long livroId = livros.findAll().getFirst().getId();
        Long leitorId = leitores.findAll().getFirst().getId();
        assertEquals(201, chamar("POST", "/emprestimos", "{\"livroId\":" + livroId + ",\"leitorId\":" + leitorId + "}").statusCode());
        assertTrue(chamar("GET", "/livros/" + livroId, null).body().contains("\"disponiveis\":0"));
        Long id = emprestimos.findAll().getFirst().getId();
        assertTrue(chamar("GET", "/emprestimos/" + id, null).body().contains("Dom Casmurro"));
        assertEquals(200, chamar("PATCH", "/emprestimos/" + id + "/devolucao", null).statusCode());
        assertTrue(chamar("GET", "/livros/" + livroId, null).body().contains("\"disponiveis\":1"));
        assertEquals(409, chamar("PATCH", "/emprestimos/" + id + "/devolucao", null).statusCode());
    }

    @Test void errosHttpCoerentes() throws Exception {
        assertEquals(400, chamar("POST", "/livros", "{}").statusCode());
        assertEquals(400, chamar("POST", "/leitores", "{\"nome\":\"Ana\",\"email\":\"invalido\"}").statusCode());
        assertEquals(400, chamar("POST", "/emprestimos", "{erro}").statusCode());
        assertEquals(400, chamar("GET", "/livros/abc", null).statusCode());
        assertEquals(404, chamar("GET", "/livros/999999", null).statusCode());
        livro("9788535914849", 1);
        var duplicado = chamar("POST", "/livros", """
            {"titulo":"Outro","autor":"Autor","isbn":"9788535914849","categoria":"Romance","exemplares":1}
            """);
        assertEquals(409, duplicado.statusCode());
        assertTrue(duplicado.body().contains("mensagem"));
    }

    @Test void naoDuplicaEmprestimoAtivoDoMesmoLivro() {
        var livro = livro("9788535914849", 2); var leitor = leitor("ana@exemplo.com");
        var request = new EmprestimoRequest(livro.getId(), leitor.getId()); service.criar(request);
        assertThrows(RegraNegocioException.class, () -> service.criar(request));
        assertEquals(1, emprestimos.count());
    }

    @Test void devolucaoLiberaNovaRetiradaDoMesmoLivro() {
        var livro = livro("9788535914849", 1); var leitor = leitor("ana@exemplo.com");
        var request = new EmprestimoRequest(livro.getId(), leitor.getId());
        service.devolver(service.criar(request).id());
        assertDoesNotThrow(() -> service.criar(request));
        assertEquals(1, emprestimos.countByLivroIdAndDevolvidoEmIsNull(livro.getId()));
    }

    @Test void atrasoPersistidoBloqueiaNovaRetirada() {
        var antigo = livro("9788535914849", 1); var novo = livro("9788535914856", 1);
        var leitor = leitor("ana@exemplo.com");
        emprestimos.save(new Emprestimo(antigo, leitor, LocalDate.now(clock).minusDays(15)));
        assertThrows(RegraNegocioException.class, () -> service.criar(new EmprestimoRequest(novo.getId(), leitor.getId())));
    }

    @Test void limiteDeTresProtegidoPeloService() {
        var leitor = leitor("ana@exemplo.com");
        for (int i = 0; i < 3; i++) service.criar(new EmprestimoRequest(livro("978853591480" + i, 1).getId(), leitor.getId()));
        var quarto = livro("9788535914803", 1);
        assertThrows(RegraNegocioException.class, () -> service.criar(new EmprestimoRequest(quarto.getId(), leitor.getId())));
        assertEquals(3, emprestimos.count());
    }

    @Test void atualizacoesRespeitamEmprestimosEmAberto() {
        var livro = livro("9788535914849", 2); var a = leitor("a@exemplo.com"); var b = leitor("b@exemplo.com");
        service.criar(new EmprestimoRequest(livro.getId(), a.getId())); service.criar(new EmprestimoRequest(livro.getId(), b.getId()));
        assertThrows(RegraNegocioException.class, () -> livroService.atualizar(livro.getId(), new LivroRequest("Livro", "Autor", livro.getIsbn(), "Romance", 1)));
        assertThrows(RegraNegocioException.class, () -> leitorService.alterarAtividade(a.getId(), false));
    }

    @Test void duasRetiradasConcorrentesNaoEmprestamMesmoExemplar() throws Exception {
        var livro = livro("9788535914849", 1);
        var a = leitor("a@exemplo.com"); var b = leitor("b@exemplo.com");
        var inicio = new CountDownLatch(1);
        try (var pool = Executors.newFixedThreadPool(2)) {
            List<Future<Boolean>> resultados = List.of(
                pool.submit(() -> retirarAposSinal(inicio, livro.getId(), a.getId())),
                pool.submit(() -> retirarAposSinal(inicio, livro.getId(), b.getId())));
            inicio.countDown(); int sucessos = 0;
            for (var futuro : resultados) if (futuro.get(15, TimeUnit.SECONDS)) sucessos++;
            assertEquals(1, sucessos);
            assertEquals(1, emprestimos.countByLivroIdAndDevolvidoEmIsNull(livro.getId()));
        }
    }
    private boolean retirarAposSinal(CountDownLatch inicio, Long livroId, Long leitorId) throws InterruptedException {
        inicio.await();
        try { service.criar(new EmprestimoRequest(livroId, leitorId)); return true; }
        catch (RegraNegocioException e) { return false; }
    }
}
