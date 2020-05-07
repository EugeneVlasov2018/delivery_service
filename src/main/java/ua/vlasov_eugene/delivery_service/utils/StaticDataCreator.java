package ua.vlasov_eugene.delivery_service.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StaticDataCreator {
	public String getUUID (){
		return UUID.randomUUID().toString();
	}
}
