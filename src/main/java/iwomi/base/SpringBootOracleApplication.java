package iwomi.base;

//import java.util.TimeZone;
//import javax.annotation.PostConstruct;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableConfigurationProperties
//@EnableSwagger2
@EntityScan(basePackages = {"iwomi.base.objects"})  // scan JPA entities
public class SpringBootOracleApplication extends SpringBootServletInitializer {

//    @PostConstruct
//    public void init() {
//        // Setting Spring Boot SetTimeZone
//        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
//    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringBootOracleApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootOracleApplication.class, args);
//        ReadMessagesAsync example = new ReadMessagesAsync();   
//        example.doIt();
    }
}
