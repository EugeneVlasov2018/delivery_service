package ua.vlasov_eugene.delivery_service.repositories;

import org.springframework.stereotype.Repository;
import ua.vlasov_eugene.delivery_service.entityes.VehicleCrew;
import ua.vlasov_eugene.delivery_service.enums.RouteStatus;
import ua.vlasov_eugene.delivery_service.utils.Page;

@Repository
public class CrewRepository {
	public Page<VehicleCrew> getAllCrews(Long numberOfPage, Long elementsInPage) {
		return null;
	}

	public Page<VehicleCrew> getAllCrewsWithCurrentStatus(RouteStatus routeStatus,
														  Long numberOfPage,
														  Long elementsInPage) {
		return null;
	}

	public VehicleCrew getCrewById(Long id) {
		return null;
	}

	public VehicleCrew updateCrew(VehicleCrew oldVersionOfCrew) {
		return null;
	}

	public void deleteCrewById(Long id) {

	}

	public VehicleCrew createNewCrew(VehicleCrew result) {
		return null;
	}
}
