package ua.vlasov_eugene.delivery_service.entities;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Courier {
	private Long id;
	private String firstNameLastName;
	private String phoneNumber;
}
