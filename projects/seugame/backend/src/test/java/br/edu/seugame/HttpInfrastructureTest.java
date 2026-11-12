package br.edu.seugame;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import br.edu.seugame.controller.StatusController;
import br.edu.seugame.exception.GlobalExceptionHandler;
import br.edu.seugame.service.StatusService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

class HttpInfrastructureTest {
    private final JsonMapper json = new JsonMapper();
    private LocalValidatorFactoryBean validator;
    private MockMvc mvc;

    @BeforeEach
    void configurarMvc() {
        validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        mvc = MockMvcBuilders.standaloneSetup(
                        new StatusController(new StatusService()), new ValidationFixture())
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @AfterEach
    void fecharValidator() {
        validator.close();
    }

    @Test
    void deveResponderStatusEmJson() throws Exception {
        MvcResult result = mvc.perform(get("/api/status")).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("SeuGame", body(result).get("application").asString());
        assertEquals("UP", body(result).get("status").asString());
    }

    @Test
    void deveRejeitarMetodoPreservandoCabecalhoAllow() throws Exception {
        MvcResult result = mvc.perform(post("/api/status")).andReturn();
        assertProblem(result, 405);
        assertTrue(result.getResponse().getHeader("Allow").contains("GET"));
    }

    @Test
    void deveResponder404ParaRotaInexistente() throws Exception {
        MvcResult result = mvc.perform(get("/api/inexistente")).andReturn();
        assertProblem(result, 404);
    }

    @Test
    void deveValidarEntradaComErroPorCampo() throws Exception {
        MvcResult result = mvc.perform(post("/test/validacao")
                .contentType(MediaType.APPLICATION_JSON).content("{\"nome\":\" \"}")).andReturn();
        assertProblem(result, 400);
        assertEquals("Informe o nome.", body(result).get("errors").get("nome").asString());
    }

    @Test
    void deveRejeitarJsonMalformadoSemExporDetalhesInternos() throws Exception {
        MvcResult result = mvc.perform(post("/test/validacao")
                .contentType(MediaType.APPLICATION_JSON).content("{json-invalido")).andReturn();
        assertProblem(result, 400);
        assertEquals("A requisição contém dados inválidos.", body(result).get("detail").asString());
    }

    @Test
    void deveRejeitarFormatoDeEntradaIncompativel() throws Exception {
        MvcResult result = mvc.perform(post("/test/validacao")
                .contentType(MediaType.TEXT_PLAIN).content("nome")).andReturn();
        assertProblem(result, 415);
    }

    @Test
    void deveOcultarDetalhesDeFalhaInesperada() throws Exception {
        MvcResult result = mvc.perform(get("/test/falha")).andReturn();
        assertProblem(result, 500);
        assertFalse(result.getResponse().getContentAsString().contains("segredo-de-teste"));
        assertFalse(body(result).has("trace"));
    }

    private JsonNode body(MvcResult result) {
        return json.readTree(new String(result.getResponse().getContentAsByteArray(), StandardCharsets.UTF_8));
    }

    private void assertProblem(MvcResult result, int status) {
        assertEquals(status, result.getResponse().getStatus());
        assertTrue(result.getResponse().getContentType().startsWith("application/problem+json"));
        assertEquals(status, body(result).get("status").asInt());
    }

    @RestController
    static class ValidationFixture {
        @PostMapping("/test/validacao")
        ValidationRequest validar(@Valid @RequestBody ValidationRequest request) {
            return request;
        }

        @GetMapping("/test/falha")
        void falhar() {
            throw new IllegalStateException("segredo-de-teste");
        }
    }

    record ValidationRequest(@NotBlank(message = "Informe o nome.") String nome) {}
}
