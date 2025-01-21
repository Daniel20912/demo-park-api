package com.danieloliveira.demo_park_api.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration // indica que é uma classe de configuração
public class SpringTimeZoneConfig {

    @PostConstruct // faz com que após a classe ser inicializada pelo spring, o metodo construtor dela é executado, e após isso o primeiro metodo a ser executado será o timeZoneConfig
    public void TimeZoneConfig() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
    }
}
