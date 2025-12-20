package org.example.backend_dip;

import com.zaxxer.hikari.HikariConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class BackendDipApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendDipApplication.class, args);
        HikariConfig config = new HikariConfig();
        config.setKeepaliveTime(30000);

    }

}
