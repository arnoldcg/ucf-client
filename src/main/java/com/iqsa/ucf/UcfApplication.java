package com.iqsa.ucf;

import com.iqsa.ucf.rest.model.entity.CompanyModel;
import com.iqsa.ucf.rest.model.entity.UserModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication(scanBasePackages = "com.iqsa.ucf")
@EnableJpaRepositories(basePackages = "com.iqsa.ucf")
@EntityScan(basePackageClasses = {CompanyModel.class, UserModel.class})
@EnableWebMvc
public class UcfApplication {

    public static void main(String[] args) {
        SpringApplication.run(UcfApplication.class, args);
    }

}
