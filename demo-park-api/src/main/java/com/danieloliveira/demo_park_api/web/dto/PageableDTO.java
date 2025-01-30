package com.danieloliveira.demo_park_api.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

// cria uma estrutura para representar uma página de dados paginados
@Getter
@Setter
public class PageableDTO {
    boolean first;
    boolean last;
    private List content = new ArrayList<>();
    @JsonProperty("page")
    private int number;

    private int size;

    @JsonProperty("pageElements")
    private int numberOfElements;

    private int totalPages;

    private int totalElements;
}
