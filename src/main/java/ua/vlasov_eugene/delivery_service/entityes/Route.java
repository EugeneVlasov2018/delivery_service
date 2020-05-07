package ua.vlasov_eugene.delivery_service.entityes;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class Route {
	private Long id;
	private Client routeClient;
	private List<City> routePoints;
	private LocalDateTime startRoute;
	private LocalDateTime endRoute;
}
