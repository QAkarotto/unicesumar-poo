package br.edu.seugame;

import static org.junit.jupiter.api.Assertions.*;
import br.edu.seugame.domain.*;
import br.edu.seugame.repository.UsuarioRepository;
import br.edu.seugame.security.*;
import java.net.*;
import java.net.http.*;
import java.nio.file.Path;
import java.time.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.*;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

class AuthHttpIT {
    static ConfigurableApplicationContext context;
    static String base;
    static final AtomicInteger sequence=new AtomicInteger();
    static final JsonMapper json=new JsonMapper();
    HttpClient client;
    CookieManager cookies;
    static Map<String,String> ddlInicial;

    @BeforeAll static void iniciar() {
        String url=System.getenv("TEST_HTTP_DB_URL");
        assertNotNull(url,"Use scripts/test-mysql.ps1 ou configure TEST_HTTP_DB_URL.");
        var source=new DriverManagerDataSource(url,System.getenv("TEST_DB_USER"),System.getenv().getOrDefault("TEST_DB_PASSWORD",""));
        var jdbc=new JdbcTemplate(source);
        assertTrue(jdbc.queryForObject("SELECT DATABASE()",String.class).matches("seugame_jpa_it_[a-zA-Z0-9_]+"));
        assertEquals(0,jdbc.queryForObject("SELECT COUNT(*) FROM information_schema.TABLES WHERE TABLE_SCHEMA=DATABASE()",Integer.class));
        new ResourceDatabasePopulator(new FileSystemResource(Path.of(System.getProperty("seugame.schema","../database/schema.sql")))).execute(source);
        ddlInicial=snapshot(jdbc);
        context=new SpringApplicationBuilder(SeuGameApplication.class).run("--spring.profiles.active=mysql-it","--spring.config.import=",
                "--server.port=0","--spring.datasource.url="+url,"--spring.datasource.username="+System.getenv("TEST_DB_USER"),
                "--spring.datasource.password="+System.getenv().getOrDefault("TEST_DB_PASSWORD",""));
        base="http://localhost:"+context.getEnvironment().getRequiredProperty("local.server.port");
    }
    @AfterAll static void encerrar() {
        if(context!=null)try {assertEquals(ddlInicial,snapshot(new JdbcTemplate(context.getBean(javax.sql.DataSource.class))));}
        finally {context.close();}
    }
    @BeforeEach void novoCliente() {cookies=new CookieManager(null,CookiePolicy.ACCEPT_ALL); client=HttpClient.newBuilder().cookieHandler(cookies).connectTimeout(Duration.ofSeconds(5)).build();}
    @AfterEach void limpar() {client.close();SecurityContextHolder.clearContext();}
    HttpResponse<String> request(String method,String path,Object body,String token) throws Exception {
        var builder=HttpRequest.newBuilder(URI.create(base+path)).timeout(Duration.ofSeconds(20)).header("Accept","application/json");
        if(token!=null)builder.header("X-CSRF-TOKEN",token);
        if(body!=null)builder.header("Content-Type","application/json");
        return client.send(builder.method(method,body==null?HttpRequest.BodyPublishers.noBody():HttpRequest.BodyPublishers.ofString(json.writeValueAsString(body))).build(),HttpResponse.BodyHandlers.ofString());
    }
    JsonNode body(HttpResponse<String> r) {return json.readTree(r.body());}
    String csrf() throws Exception {var r=request("GET","/api/auth/csrf",null,null);assertEquals(200,r.statusCode());return body(r).get("token").asString();}
    String nomeUsuario() {return "usuario"+sequence.incrementAndGet();}
    Map<String,String> cadastro(String usuario) {return Map.of("nome","Pessoa Teste","usuario",usuario,"email",usuario+"@example.invalid","senha","SenhaTeste123!","confirmarSenha","SenhaTeste123!");}
    JsonNode cadastrar(String usuario) throws Exception {var r=request("POST","/api/auth/cadastro",cadastro(usuario),csrf());assertEquals(201,r.statusCode(),r.body());return body(r);}

    @Test void cadastroDeveAutenticarRotacionarSessaoEGuardarSomenteHash() throws Exception {
        String usuario=nomeUsuario(),token=csrf();String anterior=cookies.getCookieStore().getCookies().getFirst().getValue();
        var r=request("POST","/api/auth/cadastro",cadastro(usuario),token);
        assertEquals(201,r.statusCode(),r.body());assertFalse(r.body().contains("senha"));assertFalse(r.body().contains("hash"));
        assertNotEquals(anterior,cookies.getCookieStore().getCookies().getFirst().getValue());
        assertTrue(r.headers().allValues("set-cookie").stream().anyMatch(c->c.toLowerCase().contains("httponly")));
        assertEquals(200,request("GET","/api/usuarios/me",null,null).statusCode());
        var u=context.getBean(UsuarioRepository.class).findByUsuario(usuario).orElseThrow();
        assertTrue(u.getSenhaHash().startsWith("$2"));assertNotEquals("SenhaTeste123!",u.getSenhaHash());
        assertTrue(context.getBean(PasswordEncoder.class).matches("SenhaTeste123!",u.getSenhaHash()));
        assertEquals(403,request("PATCH","/api/usuarios/me",Map.of("nome","Novo Nome","email","outro@example.invalid"),token).statusCode());
    }
    @Test void cadastroInvalidoEDuplicadosDevemSerRejeitados() throws Exception {
        String usuario=nomeUsuario();var dados=new HashMap<>(cadastro(usuario));dados.put("confirmarSenha","diferente");
        assertEquals(400,request("POST","/api/auth/cadastro",dados,csrf()).statusCode());
        assertTrue(context.getBean(UsuarioRepository.class).findByUsuario(usuario).isEmpty());
        cadastrar(usuario);
        assertEquals(409,request("POST","/api/auth/cadastro",cadastro(usuario),csrf()).statusCode());
        dados=new HashMap<>(cadastro(nomeUsuario()));dados.put("email",usuario+"@example.invalid");
        assertEquals(409,request("POST","/api/auth/cadastro",dados,csrf()).statusCode());
        dados=new HashMap<>(cadastro(nomeUsuario()));dados.put("senha","é".repeat(40));dados.put("confirmarSenha","é".repeat(40));
        assertEquals(400,request("POST","/api/auth/cadastro",dados,csrf()).statusCode());
    }
    @Test void loginPorUsuarioEmailSenhaIncorretaELogout() throws Exception {
        String usuario=nomeUsuario();cadastrar(usuario);
        assertEquals(204,request("POST","/api/auth/logout",null,csrf()).statusCode());
        assertEquals(401,request("GET","/api/usuarios/me",null,null).statusCode());
        assertEquals(401,request("POST","/api/auth/login",Map.of("identificador",usuario,"senha","errada"),csrf()).statusCode());
        assertEquals(200,request("POST","/api/auth/login",Map.of("identificador",usuario.toUpperCase(Locale.ROOT),"senha","SenhaTeste123!"),csrf()).statusCode());
        assertEquals(204,request("POST","/api/auth/logout",null,csrf()).statusCode());
        assertEquals(200,request("POST","/api/auth/login",Map.of("identificador",usuario+"@example.invalid","senha","SenhaTeste123!"),csrf()).statusCode());
    }
    @Test void deveExigirAutenticacaoECsrf() throws Exception {
        assertEquals(401,request("GET","/api/usuarios/me",null,null).statusCode());
        assertEquals(403,request("POST","/api/auth/cadastro",cadastro(nomeUsuario()),null).statusCode());
        assertEquals(403,request("POST","/api/auth/login",Map.of("identificador","inexistente","senha","errada"),"invalido").statusCode());
        assertEquals(403,request("POST","/api/auth/logout",null,null).statusCode());
    }
    @Test void perfilDeveSerAtomicoEUsuarioImutavel() throws Exception {
        String usuario=nomeUsuario();JsonNode antes=cadastrar(usuario);
        assertEquals(400,request("PATCH","/api/usuarios/me",Map.of("nome","x","email","novo@example.invalid"),csrf()).statusCode());
        assertEquals(antes.get("nome").asString(),body(request("GET","/api/usuarios/me",null,null)).get("nome").asString());
        var r=request("PATCH","/api/usuarios/me",Map.of("nome","Nome Alterado","email",nomeUsuario()+"@example.invalid","usuario","outro"),csrf());
        assertEquals(200,r.statusCode(),r.body());assertEquals(usuario,body(r).get("usuario").asString());
        assertEquals(200,request("GET","/api/usuarios/me/torneios",null,null).statusCode());
    }
    @Test void deveRejeitarGerenciamentoPorOutroUsuario() throws Exception {
        String primeiro=nomeUsuario();cadastrar(primeiro);
        String segundo=nomeUsuario();cadastrar(segundo);
        var repo=context.getBean(UsuarioRepository.class);
        var a=repo.findByUsuario(primeiro).orElseThrow();var b=repo.findByUsuario(segundo).orElseThrow();
        var t=new Torneio(b,"Torneio privado para alterações","",TipoTorneio.TABELA,LocalDate.of(2030,1,1),Clock.systemUTC());
        var auth=UsernamePasswordAuthenticationToken.authenticated(new UsuarioAutenticado(a),null,List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);
        assertThrows(org.springframework.security.access.AccessDeniedException.class,()->SessaoUsuario.exigirAutoria(t));
    }
    @Test void corsDevePermitirApenasOrigemConfigurada() throws Exception {
        for(String origin:List.of("http://localhost:5173","https://origem-nao-autorizada.invalid")) {
            var request=HttpRequest.newBuilder(URI.create(base+"/api/usuarios/me")).method("OPTIONS",HttpRequest.BodyPublishers.noBody())
                    .header("Origin",origin).header("Access-Control-Request-Method","PATCH").header("Access-Control-Request-Headers","Content-Type,X-CSRF-TOKEN").build();
            var r=client.send(request,HttpResponse.BodyHandlers.ofString());
            if(origin.contains("localhost")) {assertEquals(200,r.statusCode());assertEquals("true",r.headers().firstValue("Access-Control-Allow-Credentials").orElseThrow());}
            else assertEquals(403,r.statusCode());
        }
    }
    static Map<String,String> snapshot(JdbcTemplate jdbc) {
        Map<String,String> result=new LinkedHashMap<>();
        for(String t:List.of("usuarios","torneios","participantes","partidas","classificacao"))
            result.put(t,jdbc.queryForMap("SHOW CREATE TABLE "+t).get("Create Table").toString().replaceAll("AUTO_INCREMENT=\\d+\\s*",""));
        return result;
    }

    Map<String,Object> torneio(String nome,String tipo,List<String> participantes) {
        return new HashMap<>(Map.of("nome",nome,"descricao","Descrição inicial","tipo",tipo,
                "dataInicio","2035-01-01","participantes",participantes));
    }
    JsonNode criar(Map<String,Object> dados) throws Exception {
        var r=request("POST","/api/torneios",dados,csrf());assertEquals(201,r.statusCode(),r.body());
        assertEquals("/api/torneios/"+body(r).get("torneio").get("id").asLong(),r.headers().firstValue("Location").orElseThrow());
        return body(r);
    }
    String caminho(JsonNode criado) {return "/api/torneios/"+criado.get("torneio").get("id").asLong();}
    JsonNode consultar(String path) throws Exception {
        var r=request("GET",path,null,null);assertEquals(200,r.statusCode(),r.body());return body(r);
    }
    HttpResponse<String> resultado(String path,JsonNode partida,int primeiro,int segundo) throws Exception {
        return request("PUT",path+"/partidas/"+partida.get("id").asLong()+"/resultado",Map.of("placar1",primeiro,"placar2",segundo),csrf());
    }
    JdbcTemplate jdbc() {return new JdbcTemplate(context.getBean(javax.sql.DataSource.class));}

    @Test void criarConsultarPublicamenteEFiltrarSemExporEntidades() throws Exception {
        cadastrar(nomeUsuario());String nome="Público "+nomeUsuario();
        var criado=criar(torneio(nome,"TABELA",List.of("Ana","Bruno","Caio")));String path=caminho(criado);
        assertEquals(3,criado.get("participantes").size());assertEquals(3,consultar(path+"/partidas").size());
        var meus=consultar("/api/usuarios/me/torneios");assertEquals(1,meus.size());
        assertEquals(3,meus.get(0).get("totalParticipantes").asInt());assertEquals(0,meus.get(0).get("resultados").asLong());
        assertTrue(meus.get(0).get("campeao").isNull());
        for(var c:consultar(path+"/classificacao")) {assertEquals(0,c.get("jogos").asInt());assertEquals(0,c.get("pontos").asInt());}
        assertEquals(409,request("GET",path+"/chaveamento",null,null).statusCode());
        assertEquals(204,request("POST","/api/auth/logout",null,csrf()).statusCode());
        assertEquals(criado,consultar(path));
        var lista=consultar("/api/torneios?busca="+URLEncoder.encode(nome,java.nio.charset.StandardCharsets.UTF_8)+"&tipo=TABELA&status=FUTURO");
        assertEquals(1,lista.size());assertEquals(criado.get("torneio").get("id"),lista.get(0).get("id"));
        assertFalse(lista.toString().contains("senha"));assertFalse(lista.toString().contains("email"));
        assertEquals(404,request("GET","/api/torneios/9223372036854775807",null,null).statusCode());
        assertEquals(400,request("GET","/api/torneios?tipo=INVALIDO",null,null).statusCode());
        assertEquals(401,request("POST","/api/torneios",torneio("Sem sessão","TABELA",List.of("A","B","C")),csrf()).statusCode());
    }

    @Test void editarDescricaoPreservaSorteioEReconstruirEstruturaRespeitaFks() throws Exception {
        cadastrar(nomeUsuario());var dados=torneio("Editar "+nomeUsuario(),"TABELA",List.of("Ana","Bruno","Caio"));
        var criado=criar(dados);String path=caminho(criado);var jogos=consultar(path+"/partidas");
        dados.put("nome","Título alterado");dados.put("descricao","Nova descrição");dados.put("dataInicio","2036-03-02");
        dados.put("participantes",List.of("Caio","Ana","Bruno"));
        var r=request("PUT",path,dados,csrf());assertEquals(200,r.statusCode(),r.body());
        assertEquals(criado.get("participantes"),body(r).get("participantes"));assertEquals(jogos,consultar(path+"/partidas"));
        dados.put("tipo","CHAVEAMENTO");r=request("PUT",path,dados,csrf());assertEquals(200,r.statusCode(),r.body());
        assertEquals(criado.get("participantes"),body(r).get("participantes"));
        assertEquals(2,consultar(path+"/chaveamento").size());assertEquals(409,request("GET",path+"/classificacao",null,null).statusCode());
        dados.put("participantes",List.of("Dora","Edu","Fábio","Gabi","Hugo"));
        r=request("PUT",path,dados,csrf());assertEquals(200,r.statusCode(),r.body());assertEquals(5,body(r).get("participantes").size());
        assertEquals(7,consultar(path+"/partidas").size());
        assertEquals(204,request("DELETE",path,null,csrf()).statusCode());
        long id=criado.get("torneio").get("id").asLong();
        for(String tabela:List.of("participantes","partidas","classificacao"))assertEquals(0,jdbc().queryForObject("SELECT COUNT(*) FROM "+tabela+" WHERE torneio_id=?",Integer.class,id));
        assertEquals(404,request("GET",path,null,null).statusCode());
    }

    @Test void somenteCriadorPodeAlterarExcluirPontuarOuFinalizar() throws Exception {
        cadastrar(nomeUsuario());var dados=torneio("Autoria "+nomeUsuario(),"TABELA",List.of("Ana","Bruno","Caio"));
        var criado=criar(dados);String path=caminho(criado);var jogos=consultar(path+"/partidas");
        cadastrar(nomeUsuario());
        assertEquals(403,request("PUT",path,dados,csrf()).statusCode());
        assertEquals(403,request("DELETE",path,null,csrf()).statusCode());
        assertEquals(403,resultado(path,jogos.get(0),2,0).statusCode());
        assertEquals(403,request("POST",path+"/finalizacao",null,csrf()).statusCode());
        assertEquals(criado,consultar(path));assertEquals(jogos,consultar(path+"/partidas"));
        assertEquals(0,consultar("/api/usuarios/me/torneios").size());
        assertEquals(404,request("GET","/api/ranking",null,null).statusCode());
    }

    @Test void tabelaRecalculaCorrecaoFinalizaEBloqueiaMutacoes() throws Exception {
        cadastrar(nomeUsuario());var dados=torneio("Tabela "+nomeUsuario(),"TABELA",List.of("Ana","Bruno","Caio"));
        var criado=criar(dados);String path=caminho(criado);var jogos=consultar(path+"/partidas");
        assertEquals(409,request("POST",path+"/finalizacao",null,csrf()).statusCode());
        for(var p:jogos)assertEquals(200,resultado(path,p,0,0).statusCode());
        assertEquals("Ana",consultar(path+"/classificacao").get(0).get("participante").get("nome").asString());
        assertEquals(200,resultado(path,jogos.get(0),3,0).statusCode());
        var tabela=consultar(path+"/classificacao");var lider=tabela.get(0);
        assertEquals(jogos.get(0).get("participante1").get("id"),lider.get("participante").get("id"));
        assertEquals(4,lider.get("pontos").asInt());assertEquals(2,lider.get("jogos").asInt());assertEquals(3,lider.get("saldo").asInt());
        dados.put("descricao","Pode mudar após resultados");assertEquals(200,request("PUT",path,dados,csrf()).statusCode());
        dados.put("participantes",List.of("Outro","Bruno","Caio"));assertEquals(409,request("PUT",path,dados,csrf()).statusCode());
        var finalizado=request("POST",path+"/finalizacao",null,csrf());assertEquals(200,finalizado.statusCode(),finalizado.body());
        assertEquals("FINALIZADO",body(finalizado).get("torneio").get("status").asString());
        assertEquals(lider.get("participante"),body(finalizado).get("torneio").get("campeao"));
        assertEquals(body(finalizado),consultar(path));
        var resumoFinal=consultar("/api/usuarios/me/torneios").get(0);
        assertEquals(3,resumoFinal.get("resultados").asLong());assertEquals(lider.get("participante"),resumoFinal.get("campeao"));
        assertEquals(409,resultado(path,jogos.get(0),0,4).statusCode());
        assertEquals(409,request("PUT",path,dados,csrf()).statusCode());
        assertEquals(409,request("DELETE",path,null,csrf()).statusCode());
        assertEquals(409,request("POST",path+"/finalizacao",null,csrf()).statusCode());
        assertEquals(tabela,consultar(path+"/classificacao"));
    }

    @Test void tabelaEmpatadaDefineCampeaoPeloNome() throws Exception {
        cadastrar(nomeUsuario());String path=caminho(criar(torneio("Empate "+nomeUsuario(),"TABELA",List.of("Zeca","Ana","Bruno"))));
        for(var p:consultar(path+"/partidas"))assertEquals(200,resultado(path,p,0,0).statusCode());
        var r=request("POST",path+"/finalizacao",null,csrf());assertEquals(200,r.statusCode(),r.body());
        assertEquals("Ana",body(r).get("torneio").get("campeao").get("nome").asString());
    }

    @Test void chaveamentoPreservaByesSemPlacarEPropagaAteCampeao() throws Exception {
        cadastrar(nomeUsuario());String path=caminho(criar(torneio("Chave "+nomeUsuario(),"CHAVEAMENTO",List.of("A","B","C","D","E"))));
        int byes=0;
        for(var p:consultar(path+"/partidas"))if(p.get("rodada").asInt()==1 && (p.get("participante1").isNull()||p.get("participante2").isNull())) {
            byes++;assertTrue(p.get("placar1").isNull());assertTrue(p.get("placar2").isNull());assertFalse(p.get("vencedor").isNull());
            assertEquals(409,resultado(path,p,1,0).statusCode());
        }
        assertEquals(3,byes);assertEquals(409,request("POST",path+"/finalizacao",null,csrf()).statusCode());
        for(int rodada=1;rodada<=3;rodada++)for(var p:consultar(path+"/partidas"))if(p.get("rodada").asInt()==rodada && p.get("vencedor").isNull()) {
            assertEquals(400,resultado(path,p,0,0).statusCode());assertEquals(200,resultado(path,p,2,0).statusCode());
        }
        var jogos=consultar(path+"/partidas");var finalDaChave=jogos.get(jogos.size()-1);
        var r=request("POST",path+"/finalizacao",null,csrf());assertEquals(200,r.statusCode(),r.body());
        assertEquals(finalDaChave.get("vencedor"),body(r).get("torneio").get("campeao"));
    }

    @Test void corrigirChaveLimpaApenasRamoDependente() throws Exception {
        cadastrar(nomeUsuario());String path=caminho(criar(torneio("Correção "+nomeUsuario(),"CHAVEAMENTO",List.of("A","B","C","D","E","F","G","H"))));
        for(int rodada=1;rodada<=3;rodada++)for(var p:consultar(path+"/partidas"))if(p.get("rodada").asInt()==rodada)assertEquals(200,resultado(path,p,2,0).statusCode());
        var antes=consultar(path+"/partidas");assertEquals(200,resultado(path,antes.get(0),0,2).statusCode());
        var depois=consultar(path+"/partidas");
        assertTrue(depois.get(4).get("placar1").isNull());assertEquals(antes.get(5),depois.get(5));
        assertTrue(depois.get(6).get("placar1").isNull());assertTrue(depois.get(6).get("vencedor").isNull());
        assertEquals(409,request("POST",path+"/finalizacao",null,csrf()).statusCode());
        for(int rodada=2;rodada<=3;rodada++)for(var p:consultar(path+"/partidas"))if(p.get("rodada").asInt()==rodada && p.get("vencedor").isNull())assertEquals(200,resultado(path,p,1,0).statusCode());
        assertEquals(200,request("POST",path+"/finalizacao",null,csrf()).statusCode());
    }

    @Test void falhaAposInsertOuMutacaoDeveFazerRollbackIntegral() throws Exception {
        cadastrar(nomeUsuario());int antes=jdbc().queryForObject("SELECT COUNT(*) FROM torneios",Integer.class);
        var r=request("POST","/api/torneios",torneio("Rollback "+nomeUsuario(),"TABELA",List.of("Ána","A\u0301na","Outro")),csrf());
        assertEquals(409,r.statusCode(),r.body());assertEquals(antes,jdbc().queryForObject("SELECT COUNT(*) FROM torneios",Integer.class));
        assertFalse(r.body().contains("SQL"));assertFalse(r.body().contains("constraint"));
        var dados=torneio("Rollback edição "+nomeUsuario(),"TABELA",List.of("A","B","C"));var criado=criar(dados);String path=caminho(criado);
        var partidasAntes=consultar(path+"/partidas");dados.put("tipo","CHAVEAMENTO");dados.put("nome"," a ");
        assertEquals(400,request("PUT",path,dados,csrf()).statusCode());
        assertEquals(criado,consultar(path));assertEquals(partidasAntes,consultar(path+"/partidas"));
    }

    @Test void resultadosConcorrentesNaoPerdemAtualizacoes() throws Exception {
        cadastrar(nomeUsuario());String path=caminho(criar(torneio("Concorrência "+nomeUsuario(),"TABELA",List.of("A","B","C","D"))));
        var jogos=consultar(path+"/partidas");String token=csrf();
        var tarefas=new ArrayList<java.util.concurrent.CompletableFuture<HttpResponse<String>>>();
        for(int i=0;i<2;i++)tarefas.add(client.sendAsync(HttpRequest.newBuilder(URI.create(base+path+"/partidas/"+jogos.get(i).get("id").asLong()+"/resultado"))
                .timeout(Duration.ofSeconds(20)).header("Content-Type","application/json").header("X-CSRF-TOKEN",token)
                .PUT(HttpRequest.BodyPublishers.ofString("{\"placar1\":2,\"placar2\":0}")).build(),HttpResponse.BodyHandlers.ofString()));
        for(var tarefa:tarefas){var r=tarefa.get(25,java.util.concurrent.TimeUnit.SECONDS);assertEquals(200,r.statusCode(),r.body());}
        var atual=consultar(path+"/partidas");assertEquals(2,atual.get(0).get("placar1").asInt());assertEquals(2,atual.get(1).get("placar1").asInt());
        int jogosContabilizados=0,pontos=0;for(var c:consultar(path+"/classificacao")){jogosContabilizados+=c.get("jogos").asInt();pontos+=c.get("pontos").asInt();}
        assertEquals(4,jogosContabilizados);assertEquals(6,pontos);
    }

    @Test void getsProjetamEstadoPorDataSemGravarNoBanco() throws Exception {
        cadastrar(nomeUsuario());var criado=criar(torneio("Somente leitura "+nomeUsuario(),"TABELA",List.of("A","B","C")));String path=caminho(criado);
        long id=criado.get("torneio").get("id").asLong();jdbc().update("UPDATE torneios SET data_inicio='2020-01-01' WHERE id=?",id);
        assertEquals("ANDAMENTO",consultar(path).get("torneio").get("status").asString());
        var filtrados=consultar("/api/torneios?status=ANDAMENTO&busca="+URLEncoder.encode(criado.get("torneio").get("nome").asString(),java.nio.charset.StandardCharsets.UTF_8));
        assertEquals(1,filtrados.size());consultar(path+"/partidas");consultar(path+"/classificacao");consultar("/api/usuarios/me/torneios");
        assertEquals("FUTURO",jdbc().queryForObject("SELECT status FROM torneios WHERE id=?",String.class,id));
    }

    @Test void entradasInvalidasEPartidaDeOutroTorneioNaoAlteramDados() throws Exception {
        cadastrar(nomeUsuario());
        var dados=torneio("Validações "+nomeUsuario(),"TABELA",List.of(" Ana ","ana","Bruno"));
        assertEquals(409,request("POST","/api/torneios",dados,csrf()).statusCode());
        dados.put("participantes",List.of("Ana","Bruno","Caio"));var criado=criar(dados);String path=caminho(criado);
        String outro=caminho(criar(torneio("Outro "+nomeUsuario(),"TABELA",List.of("Ana","Bruno","Caio"))));
        var p=consultar(path+"/partidas").get(0);
        assertEquals(404,resultado(outro,p,1,0).statusCode());
        assertEquals(400,resultado(path,p,-1,0).statusCode());assertEquals(400,resultado(path,p,1000,0).statusCode());
        assertEquals(400,request("PUT",path+"/partidas/"+p.get("id").asLong()+"/resultado",Map.of("placar1",1),csrf()).statusCode());
        assertTrue(consultar(path+"/partidas").get(0).get("placar1").isNull());
        assertEquals(200,resultado(path,p,1,0).statusCode());
        assertEquals(204,request("DELETE",path,null,csrf()).statusCode());
        assertEquals(404,request("GET",path,null,null).statusCode());
    }
}
