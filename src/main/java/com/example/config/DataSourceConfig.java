package com.example.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    /*
    Con este bean podemos sobre escribir la configuración del datasource igual que haríamos por properties, si no lo hicieramos se cogerán los
    valores por defecto.
     */
    @Bean
    public DataSource getDataSource() {
        /*  Forma tradicional:
        DataSourceBuilder dataSourceBuilder =  DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.h2.Driver");
        dataSourceBuilder.url("jdbc:h2:./db/database");
        dataSourceBuilder.username("sa");
        dataSourceBuilder.password("");

        return dataSourceBuilder.build();
        */

        // También se podría hacer con el builder Sintaxis Fluent:
        return DataSourceBuilder.create()
                .driverClassName("org.h2.Driver")
                .url("jdbc:h2:./db/database")
                .username("sa")
                .password("")
                .build();

    }
}
