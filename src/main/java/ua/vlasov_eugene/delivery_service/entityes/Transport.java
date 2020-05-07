package ua.vlasov_eugene.delivery_service.entityes;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Transport {
	private Long id;
	private String numberOfVencile;
	private VehicleCrew crew;
	private Route route;
}
