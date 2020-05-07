package ua.vlasov_eugene.delivery_service.dtos;

import lombok.Data;
import lombok.experimental.Accessors;
import ua.vlasov_eugene.delivery_service.entityes.Route;

@Data
@Accessors(chain = true)
public class OldAndNewRouteDto {
	private Route oldVersionRoute;
	private Route newVersionOfRoute;
}
