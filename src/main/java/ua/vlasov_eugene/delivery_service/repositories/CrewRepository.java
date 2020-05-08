package ua.vlasov_eugene.delivery_service.repositories;

import org.springframework.stereotype.Repository;
import ua.vlasov_eugene.delivery_service.dtos.CrewDto;
import ua.vlasov_eugene.delivery_service.enums.RouteStatus;
import ua.vlasov_eugene.delivery_service.utils.Page;

@Repository
public class CrewRepository {
	public Page<CrewDto> getAllCrews(Long numberOfPage, Long elementsInPage) {
		return null;
	}

	public Page<CrewDto> getAllCrewsWithCurrentStatus(RouteStatus routeStatus,
													  Long numberOfPage,
													  Long elementsInPage) {
		return null;
	}

	public CrewDto getCrewById(Long id) {
		return null;
	}

	public CrewDto updateCrew(CrewDto oldVersionOfCrew) {
		return null;
	}

	public void deleteCrewById(Long id) {

	}

	public CrewDto createNewCrew(CrewDto result) {
		return null;
	}
}
