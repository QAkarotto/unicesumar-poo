package br.edu.agroassist.config;

import br.edu.agroassist.service.DemoService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DadosIniciaisConfig {
    @Bean
    ApplicationRunner carregarDadosDeExemplo(DemoService demoService) {
        return args -> demoService.inicializar();
    }
}
