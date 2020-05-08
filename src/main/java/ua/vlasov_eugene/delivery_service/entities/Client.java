package ua.vlasov_eugene.delivery_service.entities;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Client {
	Long id;
	String name;
	String address;
	String phoneNumber;
}
