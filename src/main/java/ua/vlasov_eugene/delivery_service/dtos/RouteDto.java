package ua.vlasov_eugene.delivery_service.dtos;

import lombok.Data;
import lombok.experimental.Accessors;
import ua.vlasov_eugene.delivery_service.entities.Client;
import ua.vlasov_eugene.delivery_service.entities.Transport;
import ua.vlasov_eugene.delivery_service.enums.RouteStatus;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
public class RouteDto {
	private Long id;
	private Date start;
	private Date end;
	private List<Client> routeClient;
	private RouteStatus status;
	private Transport transport;
}
