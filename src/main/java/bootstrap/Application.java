/**
* Main class used to start application
* <p>
* These classes contain the entry point
* </p>
*
* @since 1.0
* @author Alex Pinta, Oleh Pinta
* @version 1.0
*/
package bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Spring Boot main bootstrap.Application
 *
 */
@Configuration @EnableAutoConfiguration @EnableWebMvc
@ComponentScan(basePackages = { "controller", "helper" })
@PropertySource("classpath:/settings/application.properties")
public class Application {
    public static void main( String[] args ) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}
