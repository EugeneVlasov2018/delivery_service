package ua.vlasov_eugene.delivery_service.dtos;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TransportDto {
	private Long id;
	private String RegistrationNumber;
	private CrewDto crew;
}
