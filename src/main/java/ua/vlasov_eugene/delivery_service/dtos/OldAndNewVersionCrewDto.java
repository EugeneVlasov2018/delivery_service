package ua.vlasov_eugene.delivery_service.dtos;

import lombok.Data;
import lombok.experimental.Accessors;
import ua.vlasov_eugene.delivery_service.entityes.VehicleCrew;

@Data
@Accessors(chain = true)
public class OldAndNewVersionCrewDto {
	private VehicleCrew oldVersion;
	private VehicleCrew newVersion;
}
