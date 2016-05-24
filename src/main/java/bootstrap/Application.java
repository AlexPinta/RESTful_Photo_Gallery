package bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Spring Boot main bootstrap.Application
 *
 */
@Configuration @EnableAutoConfiguration @EnableWebMvc
@ComponentScan(basePackages = { "controller" })
@ImportResource("classpath:/settings/bean.xml")
public class Application {
    public static void main( String[] args ) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}
