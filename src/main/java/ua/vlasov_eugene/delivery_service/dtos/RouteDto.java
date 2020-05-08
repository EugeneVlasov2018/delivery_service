package ua.vlasov_eugene.delivery_service.dtos;

import lombok.Data;
import lombok.experimental.Accessors;
import ua.vlasov_eugene.delivery_service.enums.RouteStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class RouteDto {
	private Long id;
	private LocalDateTime start;
	private LocalDateTime end;
	private List<ClientDto> routeClientDto;
	private RouteStatus status;
	private TransportDto transportDto;
}
