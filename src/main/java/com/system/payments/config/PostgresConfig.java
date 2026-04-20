package com.system.payments.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class PostgresConfig {

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
     public Connection postgresSql() throws SQLException {
         try{
             Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            return connection;
         }catch(Exception e){
             System.out.println("Error in connecting to PostgreSQL server");
             e.printStackTrace();
         }
         return null;
     }

}
