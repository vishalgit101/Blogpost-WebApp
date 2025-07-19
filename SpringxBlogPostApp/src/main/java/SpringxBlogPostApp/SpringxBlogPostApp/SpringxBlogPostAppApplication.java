package SpringxBlogPostApp.SpringxBlogPostApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import jakarta.persistence.Entity;

@SpringBootApplication
@ComponentScan(basePackages = {"repositories","services","controller", "configs", "filter", "oAuth2"})
@EntityScan(basePackages = {"entity"})
public class SpringxBlogPostAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringxBlogPostAppApplication.class, args);
	}

}

