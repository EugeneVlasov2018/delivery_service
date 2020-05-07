package ua.vlasov_eugene.delivery_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.vlasov_eugene.delivery_service.dtos.OldAndNewVersionCrewDto;
import ua.vlasov_eugene.delivery_service.entityes.Courier;
import ua.vlasov_eugene.delivery_service.entityes.VehicleCrew;
import ua.vlasov_eugene.delivery_service.enums.RouteStatus;
import ua.vlasov_eugene.delivery_service.exceptions.WrongCourierException;
import ua.vlasov_eugene.delivery_service.exceptions.WrongStatusException;
import ua.vlasov_eugene.delivery_service.repositories.CourierRepository;
import ua.vlasov_eugene.delivery_service.repositories.CrewRepository;
import ua.vlasov_eugene.delivery_service.utils.Page;
import ua.vlasov_eugene.delivery_service.utils.StaticDataCreator;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CrewService {
	private static final String WRONG_STATUS_OF_CREW = "Вы не можете менять экипаж, находящийся в поездке";
	private static final String WRONG_COURIER = "Вы не можете добавить в экипаж курьера, уже находящегося в поездке";
	private static final String SUCCESSFULLY_DELETE = "Данные о экипаже были успешно удалены";
	private final StaticDataCreator dataCreator;
	private final CrewRepository crewRepo;
	private final CourierRepository courierRepo;

	@Transactional
	public Page<VehicleCrew> getCrewByFilter(RouteStatus routeStatus, Long numberOfPage, Long elementsInPage) {
		if(routeStatus==null){
			return crewRepo.getAllCrews(numberOfPage,elementsInPage);
		}
		return crewRepo.getAllCrewsWithCurrentStatus(routeStatus,numberOfPage,elementsInPage);
	}

	@Transactional
	public VehicleCrew getCrewById(Long id) {
		return crewRepo.getCrewById(id);
	}

	@Transactional
	public OldAndNewVersionCrewDto updateCrew(VehicleCrew newVersionOfCrew) {
		VehicleCrew oldVersion = crewRepo.getCrewById(newVersionOfCrew.getId());

		checkRouteStatus(oldVersion);
		checkCouriersStatus(newVersionOfCrew);

		return new OldAndNewVersionCrewDto()
				.setOldVersion(oldVersion)
				.setNewVersion(crewRepo.updateCrew(newVersionOfCrew));
	}

	@Transactional
	public String deleteById(Long id) {
		VehicleCrew currentCrew = crewRepo.getCrewById(id);
		checkRouteStatus(currentCrew);
		//toDo прописать работу с другими сущностями
		crewRepo.deleteCrewById(id);
		return SUCCESSFULLY_DELETE;
	}

	@Transactional
	public VehicleCrew createNewCrew(List<String> couriersCode) {
		List<Courier> couriersForCrew = couriersCode.stream()
				.filter(code -> !code.isEmpty())
				.map(courierRepo::getCourierByCode)
				.collect(Collectors.toList());

		VehicleCrew result = new VehicleCrew()
				.setCode(dataCreator.getUUID())
				.setCouriers(couriersForCrew);

		return crewRepo.createNewCrew(result);
	}

	private void checkCouriersStatus(VehicleCrew newVersionOfCrew) {
		if(newVersionOfCrew.getCouriers()
				.stream()
				.anyMatch(Courier::getIsOnRoute)){
			throw new WrongCourierException(WRONG_COURIER);
		}
	}

	private void checkRouteStatus(VehicleCrew oldVersion) {
		if(oldVersion.getTransport().getRoute().getStatus() == RouteStatus.ROUTE_IN_PROCESS){
			throw new WrongStatusException(WRONG_STATUS_OF_CREW);
		}
	}
}
