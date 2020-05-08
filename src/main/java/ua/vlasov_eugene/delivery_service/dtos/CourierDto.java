package ua.vlasov_eugene.delivery_service.dtos;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CourierDto {
	private Long id;
	private String firstNameLastName;
	private String phone;
}
