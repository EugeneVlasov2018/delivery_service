package ua.vlasov_eugene.delivery_service.entityes;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Courier {
	private Long id;
	private String firstName;
	private String lastName;
	private String phone;
	private Boolean isOnRoute;
}
