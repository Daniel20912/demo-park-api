package com.danieloliveira.demo_park_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

// essa annotation indica que a classe será de testes
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // isso faz com que o tomcat seja executado em uma porta de maneira randomica

// essa annotation importa os scripts sql criados e diz quando eles devem ser excutados
@Sql(scripts = "/sql/usuarios/usuarios-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) // executa o script de insert antes de executar o teste
@Sql(scripts = "/sql/usuarios/usuarios-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) // executa o script de delete após finalizar o teste
public class UsuarioIT {

    @Autowired
    WebTestClient webClient;
}
