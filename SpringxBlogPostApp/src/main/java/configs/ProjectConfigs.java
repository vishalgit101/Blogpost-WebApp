package configs;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "proxy")
public class ProjectConfigs {
	   @Bean
	    public feign.Logger.Level feignLoggerLevel() {
	        return feign.Logger.Level.FULL;
	    }
}
