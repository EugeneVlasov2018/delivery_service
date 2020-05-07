package ua.vlasov_eugene.delivery_service.entityes;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class VehicleCrew {
	private Long id;
	private String code;
	private Transport transport;
	private List<Courier> couriers;
}
