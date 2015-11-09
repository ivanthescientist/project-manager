package com.ivanthescientist.projectmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ImportResource("/application-context.xml")
@EntityScan(basePackages = "com.ivanthescientist.projectmanager.domain.model")
@EnableJpaRepositories(basePackages = "com.ivanthescientist.projectmanager.infrastructure.repository")
public class Application  {
    public static void main(String[] args) throws Exception
    {
        SpringApplication.run(Application.class, args);
    }
}


