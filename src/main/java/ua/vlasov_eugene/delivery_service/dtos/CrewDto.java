package ua.vlasov_eugene.delivery_service.dtos;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class CrewDto {
	private Long id;
	private List<CourierDto> couriers;
}
