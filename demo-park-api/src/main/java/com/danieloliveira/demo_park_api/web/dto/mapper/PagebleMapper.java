package com.danieloliveira.demo_park_api.web.dto.mapper;

import com.danieloliveira.demo_park_api.web.dto.PagebleDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PagebleMapper {
    public static PagebleDTO toDto(Page page) {
        return new ModelMapper().map(page, PagebleDTO.class);
    }
}
