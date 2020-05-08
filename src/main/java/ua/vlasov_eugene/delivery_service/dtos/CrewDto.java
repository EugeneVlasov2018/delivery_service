package ua.vlasov_eugene.delivery_service.dtos;

import lombok.Data;
import lombok.experimental.Accessors;
import ua.vlasov_eugene.delivery_service.entities.Courier;
import ua.vlasov_eugene.delivery_service.enums.CrewStatus;

import java.util.List;

@Data
@Accessors(chain = true)
public class CrewDto {
	private Long id;
	private List<Courier> couriers;
	private CrewStatus status;
}
