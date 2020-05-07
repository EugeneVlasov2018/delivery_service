package ua.vlasov_eugene.delivery_service.services;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.vlasov_eugene.delivery_service.dtos.OldAndNewVersionCrewDto;
import ua.vlasov_eugene.delivery_service.entityes.VencileCrew;
import ua.vlasov_eugene.delivery_service.enums.CrewStatus;
import ua.vlasov_eugene.delivery_service.repositories.CrewRepository;
import ua.vlasov_eugene.delivery_service.utils.Page;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CrewService {
	private final CrewRepository crewRepo;

	@Transactional
	public Page<VencileCrew> getCrewByFilter(CrewStatus crewStatus, Long numberOfPage, Long elementsInPage) {
		if(crewStatus==null){
			return crewRepo.getAllCrews();
		}
		return null;
	}

	@Transactional
	public VencileCrew getCrewById(Long id) {
		return null;
	}

	@Transactional
	public OldAndNewVersionCrewDto updateCrew(VencileCrew oldVersionOfCrew) {
		return null;
	}

	public String deleteById(Long id) {
		return null;
	}
}
