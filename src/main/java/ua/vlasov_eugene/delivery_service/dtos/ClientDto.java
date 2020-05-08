package ua.vlasov_eugene.delivery_service.dtos;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ClientDto {
	private Long id;
	private String name;
	private String address;
	private String phoneNumber;
}
