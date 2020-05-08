package ua.vlasov_eugene.delivery_service.dtos;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OldAndNewVersionCrewDto {
	private CrewDto oldVersion;
	private CrewDto newVersion;
}
