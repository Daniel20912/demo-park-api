package com.danieloliveira.demo_park_api.repositories.projection;


// serve para nas consultas usando Page, você poder escolher apenas os dados que você deseja mostrar
public interface ClienteProjection {

    Long getId();
    String getNome();
    String getCpf();
}
