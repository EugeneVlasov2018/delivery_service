package ua.vlasov_eugene.delivery_service.config;

import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Set;

public class SwaggerConfig {
	@Bean
	public Docket documents(){
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors
						.basePackage("ua/vlasov_eugene/delivery_service/controllers"))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(apiInfo())
				.consumes(Set.of("application/json"))
				.produces(Set.of("application/json"))
				.useDefaultResponseMessages(false);
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Курьерская служба")
				.description("Тестовое задание для Rationallogistics")
				.version("1.0")
				.build();
	}
}
