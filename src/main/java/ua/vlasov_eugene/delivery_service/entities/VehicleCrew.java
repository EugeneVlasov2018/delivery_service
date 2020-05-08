package ua.vlasov_eugene.delivery_service.entities;

import lombok.Data;
import lombok.experimental.Accessors;
import ua.vlasov_eugene.delivery_service.enums.CrewStatus;

@Data
@Accessors(chain = true)
public class VehicleCrew {
	private Long id;
	private CrewStatus status;
}
