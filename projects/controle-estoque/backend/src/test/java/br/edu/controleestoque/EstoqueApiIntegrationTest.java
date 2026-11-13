package br.edu.controleestoque;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração: requisição HTTP -> Controller -> Service -> Domain ->
 * Repository -> JPA/Hibernate -> banco (H2 em modo MySQL criado pelo schema.sql).
 */
@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "file:../database/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class EstoqueApiIntegrationTest {

    @Autowired
    private MockMvc mvc;

    private long categoriaId;
    private long fornecedorId;
    private long produtoId;

    @BeforeEach
    void prepararCadastros() throws Exception {
        categoriaId = criar("/api/categorias", """
                {"nome": "Informática", "descricao": "Periféricos"}
                """);
        fornecedorId = criar("/api/fornecedores", """
                {"nome": "Tech Ltda", "cnpj": "11.222.333/0001-81", "email": "vendas@tech.com"}
                """);
        produtoId = criar("/api/produtos", """
                {"sku": "INF-01", "nome": "Mouse", "precoUnitario": 49.90,
                 "estoqueMinimo": 5, "categoriaId": %d}
                """.formatted(categoriaId));
        mvc.perform(post("/api/produtos/{id}/fornecedores/{f}", produtoId, fornecedorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fornecedores[0].cnpj").value("11222333000181"));
    }

    @Test
    @DisplayName("Entrada e saída atualizam o saldo persistido do produto")
    void fluxoDeEntradaESaidaAtualizaSaldo() throws Exception {
        movimentar("ENTRADA", 10, fornecedorId, null)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.saldoPosterior").value(10))
                .andExpect(jsonPath("$.fornecedorNome").value("Tech Ltda"));

        movimentar("SAIDA", 4, null, "Requisição interna")
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.saldoAnterior").value(10))
                .andExpect(jsonPath("$.saldoPosterior").value(6));

        mvc.perform(get("/api/produtos/{id}", produtoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.produto.quantidadeEmEstoque").value(6))
                .andExpect(jsonPath("$.produto.precisaReposicao").value(false));

        mvc.perform(get("/api/movimentacoes").param("produtoId", String.valueOf(produtoId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].tipo").value("SAIDA"));
    }

    @Test
    @DisplayName("Saída maior que o saldo retorna 422 e não altera o banco")
    void saidaMaiorQueSaldoRetorna422() throws Exception {
        movimentar("ENTRADA", 3, fornecedorId, null).andExpect(status().isCreated());

        movimentar("SAIDA", 5, null, null)
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.mensagem").value(org.hamcrest.Matchers.containsString("Estoque insuficiente")));

        mvc.perform(get("/api/produtos/{id}", produtoId))
                .andExpect(jsonPath("$.produto.quantidadeEmEstoque").value(3));
        mvc.perform(get("/api/movimentacoes"))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("Entrada de fornecedor não vinculado ao produto retorna 422")
    void entradaDeFornecedorNaoVinculadoRetorna422() throws Exception {
        long outro = criar("/api/fornecedores", """
                {"nome": "Outra Ltda", "cnpj": "44555666000199"}
                """);

        movimentar("ENTRADA", 5, outro, null)
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.mensagem").value(org.hamcrest.Matchers.containsString("não está vinculado")));
    }

    @Test
    @DisplayName("Produto com histórico não pode ser excluído (409), mas pode ser inativado com saldo zero")
    void produtoComMovimentacaoNaoPodeSerExcluido() throws Exception {
        movimentar("ENTRADA", 2, fornecedorId, null).andExpect(status().isCreated());
        movimentar("SAIDA", 2, null, null).andExpect(status().isCreated());

        mvc.perform(delete("/api/produtos/{id}", produtoId))
                .andExpect(status().isConflict());

        mvc.perform(patch("/api/produtos/{id}/inativacao", produtoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ativo").value(false));

        movimentar("ENTRADA", 1, fornecedorId, null)
                .andExpect(status().isUnprocessableContent());
    }

    @Test
    @DisplayName("Produto sem histórico pode ser excluído (204)")
    void produtoSemHistoricoPodeSerExcluido() throws Exception {
        mvc.perform(delete("/api/produtos/{id}", produtoId)).andExpect(status().isNoContent());
        mvc.perform(get("/api/produtos/{id}", produtoId)).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Categoria com produtos não pode ser excluída (409)")
    void categoriaComProdutosNaoPodeSerExcluida() throws Exception {
        mvc.perform(delete("/api/categorias/{id}", categoriaId))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Fornecedor com entradas no histórico não pode ser excluído (409)")
    void fornecedorComEntradasNaoPodeSerExcluido() throws Exception {
        movimentar("ENTRADA", 1, fornecedorId, null).andExpect(status().isCreated());

        mvc.perform(delete("/api/fornecedores/{id}", fornecedorId))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Dados inválidos retornam 400 com os campos com erro")
    void dadosInvalidosRetornam400() throws Exception {
        mvc.perform(post("/api/produtos").contentType(MediaType.APPLICATION_JSON).content("""
                        {"sku": "", "nome": "X", "precoUnitario": -1, "estoqueMinimo": 0}
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.sku").exists())
                .andExpect(jsonPath("$.campos.precoUnitario").exists())
                .andExpect(jsonPath("$.campos.categoriaId").exists());
    }

    @Test
    @DisplayName("SKU duplicado retorna 409")
    void skuDuplicadoRetorna409() throws Exception {
        mvc.perform(post("/api/produtos").contentType(MediaType.APPLICATION_JSON).content("""
                        {"sku": "inf-01", "nome": "Outro", "precoUnitario": 1, "estoqueMinimo": 0, "categoriaId": %d}
                        """.formatted(categoriaId)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Recurso inexistente retorna 404")
    void recursoInexistenteRetorna404() throws Exception {
        mvc.perform(get("/api/produtos/999")).andExpect(status().isNotFound());
        mvc.perform(post("/api/movimentacoes").contentType(MediaType.APPLICATION_JSON).content("""
                        {"produtoId": 999, "tipo": "SAIDA", "quantidade": 1}
                        """))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Produto no estoque mínimo aparece na lista de reposição")
    void produtoNoMinimoApareceNaReposicao() throws Exception {
        movimentar("ENTRADA", 5, fornecedorId, null).andExpect(status().isCreated());

        mvc.perform(get("/api/produtos/reposicao"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sku").value("INF-01"));

        mvc.perform(get("/api/produtos/resumo"))
                .andExpect(jsonPath("$.produtosParaReposicao").value(1))
                .andExpect(jsonPath("$.unidadesEmEstoque").value(5));
    }

    @Test
    @DisplayName("Ajuste usa o campo novoSaldo; a resposta traz a diferença em quantidade")
    void ajusteUsaNovoSaldo() throws Exception {
        movimentar("ENTRADA", 10, fornecedorId, null).andExpect(status().isCreated());

        mvc.perform(post("/api/movimentacoes").contentType(MediaType.APPLICATION_JSON).content("""
                        {"produtoId": %d, "tipo": "AJUSTE", "novoSaldo": 7, "observacao": "Contagem física"}
                        """.formatted(produtoId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.saldoAnterior").value(10))
                .andExpect(jsonPath("$.saldoPosterior").value(7))
                .andExpect(jsonPath("$.quantidade").value(3));
    }

    @Test
    @DisplayName("Ajuste enviado com quantidade em vez de novoSaldo retorna 400 e não altera o saldo")
    void ajusteComQuantidadeRetorna400() throws Exception {
        movimentar("ENTRADA", 10, fornecedorId, null).andExpect(status().isCreated());

        movimentar("AJUSTE", 7, null, "Contagem física")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.quantidadeCompativelComOTipo").exists())
                .andExpect(jsonPath("$.campos.novoSaldoCompativelComOTipo").exists());

        mvc.perform(get("/api/produtos/{id}", produtoId))
                .andExpect(jsonPath("$.produto.quantidadeEmEstoque").value(10));
    }

    @Test
    @DisplayName("Rota inexistente e método não suportado respondem no formato ErroResponse")
    void errosDoSpringMantemStatusEFormato() throws Exception {
        mvc.perform(get("/api/inexistente"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.mensagem").value("Rota não encontrada."));

        mvc.perform(put("/api/movimentacoes/1").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value(405))
                .andExpect(jsonPath("$.caminho").value("/api/movimentacoes/1"));
    }

    @Test
    @DisplayName("Saídas simultâneas não perdem atualização de saldo (lock pessimista)")
    void saidasSimultaneasNaoPerdemSaldo() throws Exception {
        int requisicoes = 8;
        movimentar("ENTRADA", requisicoes, fornecedorId, null).andExpect(status().isCreated());

        var largada = new CountDownLatch(1);
        try (ExecutorService executor = Executors.newFixedThreadPool(requisicoes)) {
            List<Future<Integer>> statusHttp = new ArrayList<>();
            for (int i = 0; i < requisicoes; i++) {
                statusHttp.add(executor.submit(() -> {
                    largada.await();
                    return movimentar("SAIDA", 1, null, null).andReturn().getResponse().getStatus();
                }));
            }
            largada.countDown();
            for (Future<Integer> s : statusHttp) {
                assertThat(s.get(30, TimeUnit.SECONDS)).isEqualTo(201);
            }
        }

        // Sem o lock, duas transações liam o mesmo saldo e uma saída "sumia".
        mvc.perform(get("/api/produtos/{id}", produtoId))
                .andExpect(jsonPath("$.produto.quantidadeEmEstoque").value(0));
        mvc.perform(get("/api/movimentacoes").param("tipo", "SAIDA"))
                .andExpect(jsonPath("$.length()").value(requisicoes));
    }

    // ------------------------------------------------------------------

    private long criar(String url, String json) throws Exception {
        var resposta = mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return ((Number) JsonPath.read(resposta, "$.id")).longValue();
    }

    private ResultActions movimentar(String tipo, int quantidade, Long fornecedor, String obs) throws Exception {
        String json = """
                {"produtoId": %d, "tipo": "%s", "quantidade": %d, "fornecedorId": %s, "observacao": %s}
                """.formatted(produtoId, tipo, quantidade, fornecedor,
                obs == null ? "null" : "\"" + obs + "\"");
        return mvc.perform(post("/api/movimentacoes").contentType(MediaType.APPLICATION_JSON).content(json));
    }
}
