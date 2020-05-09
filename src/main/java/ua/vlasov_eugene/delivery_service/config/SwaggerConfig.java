package ua.vlasov_eugene.delivery_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Set;
@Configuration
public class SwaggerConfig {
	@Bean
	public Docket documents(){
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors
						.withClassAnnotation(RestController.class))
				.paths(PathSelectors.any())
				//.paths(Predicates.not(PathSelectors.regex("/error.*")))
				.build()
				.apiInfo(apiInfo())
				.consumes(Set.of("application/json"))
				.produces(Set.of("application/json"))
				.useDefaultResponseMessages(false);
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Курьерская служба")
				.description("Тестовое задание для Rationallogistics\n" +
						"ВАЖНО !!!! Даты на сервере хранятся в таймзоне сервера, но возвращаются в UTC,\n" +
						"чтобы на фронте было удобнее подгонять ответ под таймзону клиента")
				.contact(new Contact("Eugene Vlasov",null,"negativ529021@gmail.com"))
				.version("1.0")
				.build();
	}
}
