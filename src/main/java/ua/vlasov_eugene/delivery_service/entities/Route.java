package ua.vlasov_eugene.delivery_service.entities;

import lombok.Data;
import lombok.experimental.Accessors;
import ua.vlasov_eugene.delivery_service.enums.RouteStatus;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class Route {
	private Long id;
	private LocalDateTime start;
	private LocalDateTime finish;
	private Long transportId;
	private RouteStatus status;
}
