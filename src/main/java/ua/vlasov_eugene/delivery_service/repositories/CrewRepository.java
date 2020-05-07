package ua.vlasov_eugene.delivery_service.repositories;

import org.springframework.stereotype.Repository;
import ua.vlasov_eugene.delivery_service.entityes.VencileCrew;
import ua.vlasov_eugene.delivery_service.enums.CrewStatus;
import ua.vlasov_eugene.delivery_service.utils.Page;

@Repository
public class CrewRepository {
	public Page<VencileCrew> getAllCrews(Long numberOfPage, Long elementsInPage) {
		return null;
	}

	public Page<VencileCrew> getAllCrewsWithCurrentStatus(CrewStatus crewStatus,
														  Long numberOfPage,
														  Long elementsInPage) {
		return null;
	}

	public VencileCrew getCrewById(Long id) {
		return null;
	}

	public VencileCrew updateCrewById(VencileCrew oldVersionOfCrew) {
		return null;
	}

	public void deleteCrewById(Long id) {

	}

	public VencileCrew createNewCrew(VencileCrew result) {
		return null;
	}
}
