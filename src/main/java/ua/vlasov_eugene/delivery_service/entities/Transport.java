package ua.vlasov_eugene.delivery_service.entities;

import lombok.Data;
import lombok.experimental.Accessors;
import ua.vlasov_eugene.delivery_service.enums.TransportStatus;

@Data
@Accessors(chain = true)
public class Transport {
	private Long id;
	private String registrationNumber;
	private Long crewId;
	private TransportStatus status;
}
