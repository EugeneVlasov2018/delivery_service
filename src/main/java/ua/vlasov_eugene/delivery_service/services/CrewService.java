package ua.vlasov_eugene.delivery_service.services;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.vlasov_eugene.delivery_service.dtos.OldAndNewVersionCrewDto;
import ua.vlasov_eugene.delivery_service.entityes.Courier;
import ua.vlasov_eugene.delivery_service.entityes.VencileCrew;
import ua.vlasov_eugene.delivery_service.enums.CrewStatus;
import ua.vlasov_eugene.delivery_service.repositories.CourierRepository;
import ua.vlasov_eugene.delivery_service.repositories.CrewRepository;
import ua.vlasov_eugene.delivery_service.utils.Page;
import ua.vlasov_eugene.delivery_service.utils.StaticDataCreator;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CrewService {
	private final StaticDataCreator dataCreator;
	private final CrewRepository crewRepo;
	private final CourierRepository courierRepo;

	@Transactional
	public Page<VencileCrew> getCrewByFilter(CrewStatus crewStatus, Long numberOfPage, Long elementsInPage) {
		if(crewStatus==null){
			return crewRepo.getAllCrews(numberOfPage,elementsInPage);
		}
		return crewRepo.getAllCrewsWithCurrentStatus(crewStatus,numberOfPage,elementsInPage);
	}

	@Transactional
	public VencileCrew getCrewById(Long id) {
		return crewRepo.getCrewById(id);
	}

	@Transactional
	public OldAndNewVersionCrewDto updateCrew(VencileCrew oldVersionOfCrew) {
		return new OldAndNewVersionCrewDto()
				.setOldVersion(oldVersionOfCrew)
				.setNewVersion(crewRepo.updateCrewById(oldVersionOfCrew));
	}

	@Transactional
	public String deleteById(Long id) {
		crewRepo.deleteCrewById(id);
		return "Your crew was successfully deleted))";
	}

	@Transactional
	public VencileCrew createNewCrew(List<String> couriersCode) {
		List<Courier> couriersForCrew = couriersCode.stream()
				.filter(code -> !code.isEmpty())
				.map(courierRepo::getCourierByCode)
				.collect(Collectors.toList());

		VencileCrew result = new VencileCrew()
				.setCode(dataCreator.getUUID())
				.setCouriers(couriersForCrew);

		return crewRepo.createNewCrew(result);
	}
}
