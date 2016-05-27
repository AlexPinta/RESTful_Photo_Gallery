package bootstrap;

//import org.apache.log4j.Logger;
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
//	final static Logger logger = Logger.getLogger(Application.class);
	
	public static void main( String[] args ) throws Exception {
//		String parameter = "Oleg!";
//	    logger.debug("This is debug : " + parameter);
//		logger.info("This is info : " + parameter);
//		logger.warn("This is warn : " + parameter);
//		logger.error("This is error : " + parameter);
//		logger.fatal("This is fatal : " + parameter);
	
        SpringApplication.run(Application.class, args);
    }
}
