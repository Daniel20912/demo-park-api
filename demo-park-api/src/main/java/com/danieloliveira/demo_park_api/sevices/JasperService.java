package com.danieloliveira.demo_park_api.sevices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class JasperService {

    // classe para ler os recursos, os arquivos presentes no projeto
    private final ResourceLoader resourceLoader;


    private final DataSource dataSource;


    // essa váriavel vai armazenar alguns parametros que serão enviados para o relatório
    private Map<String, Object> params = new HashMap<>();


    private static final String JASPER_DIRETORIO = "classpath:reports/"; // classpath em springboot é tudo o que tem na pasta resources


    // metodo para atribuir os parametros
    public void addParam(String key, Object value) {
        this.params.put("IMAGEM_DIRETORIO", JASPER_DIRETORIO + key);
        this.params.put("REPORT_LOCALE", new Locale("pt", "BR"));
        this.params.put(key, value);
    }


    // metodo para criar o pdf
    public byte[] gerarPdf() {
        byte[] bytes = null;
        try {
            Resource resource = resourceLoader.getResource(JASPER_DIRETORIO.concat("estacionamentos.jasper"));
            InputStream stream = resource.getInputStream(); // lê o relatório

            // a classe JasperFillManager e o metodo dela recebem todas as informações necessárias para que o arquivo ".jasper" receba as informações que ele está esperando
            // e também uma conexão com o banco de dados para que uma consulta seja feita
            // entõo ele vai processar essa consulta e retornar um objeto do tipo jasperPrint
            JasperPrint print = JasperFillManager.fillReport(stream, params, dataSource.getConnection());

            // o retorno irá exportar o pdf em um array de bytes
            bytes = JasperExportManager.exportReportToPdf(print);
        } catch (JRException | SQLException | IOException e) {
            log.error("Jasper Reports ::: {}", e.getMessage());
            throw new RuntimeException(e);
        }

        return bytes;
    }
}
