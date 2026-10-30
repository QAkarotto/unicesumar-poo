package br.edu.sistemaacademico.infra;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

/**
 * Cria a ConexaoSqlite e, se configurado, prepara o banco (schema + dados iniciais)
 * antes de qualquer Repository ser usado.
 */
@Configuration
public class BancoConfig {

    @Bean
    public ConexaoSqlite conexaoSqlite(
            @Value("${sistemaacademico.banco.caminho}") String caminho,
            @Value("${sistemaacademico.banco.schema}") String schema,
            @Value("${sistemaacademico.banco.dados-iniciais}") String dadosIniciais,
            @Value("${sistemaacademico.banco.inicializar:true}") boolean inicializar) {

        var conexao = new ConexaoSqlite(caminho);
        if (inicializar) {
            new InicializadorBanco(conexao).executar(Path.of(schema), Path.of(dadosIniciais));
        }
        return conexao;
    }
}
