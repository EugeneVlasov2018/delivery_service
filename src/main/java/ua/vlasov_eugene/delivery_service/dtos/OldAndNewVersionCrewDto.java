package ua.vlasov_eugene.delivery_service.dtos;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import ua.vlasov_eugene.delivery_service.entityes.VencileCrew;

@Data
@Accessors(chain = true)
public class OldAndNewVersionCrewDto {
	private VencileCrew oldVersion;
	private VencileCrew newVersion;
}
