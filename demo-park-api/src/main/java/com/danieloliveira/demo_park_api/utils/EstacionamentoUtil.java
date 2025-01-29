package com.danieloliveira.demo_park_api.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EstacionamentoUtil {

    // 24-01-16T23:48.616463500

    public static String gerarRecibo() {
        LocalDateTime date = LocalDateTime.now();
        String recibo = date.toString().substring(0, 19); // do 0 ao 19 pega: 24-01-16T23:48
        return recibo.replace("-", "")
                .replace(":", "")
                .replace("T", "-");
    }
}
