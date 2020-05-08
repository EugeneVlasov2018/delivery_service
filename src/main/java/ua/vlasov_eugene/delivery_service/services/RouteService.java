package ua.vlasov_eugene.delivery_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.vlasov_eugene.delivery_service.dtos.OldAndNewRouteDto;
import ua.vlasov_eugene.delivery_service.entities.Route;
import ua.vlasov_eugene.delivery_service.enums.RouteStatus;
import ua.vlasov_eugene.delivery_service.exceptions.WrongStatusException;
import ua.vlasov_eugene.delivery_service.dtos.RouteDto;
import ua.vlasov_eugene.delivery_service.mappers.RouteMapper;
import ua.vlasov_eugene.delivery_service.repositories.RouteRepository;
import ua.vlasov_eugene.delivery_service.utils.Page;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteService {
	private static final String SUCCESSFULLY_DELETE = "Выбранная поездка была успешно удалена";
	private static final String WRONG_ROUTE_STATUS = "Текущий статус маршрута не позволяет вам его изменить";
	private static final String ROUTE_IS_IN_ACTION = "Вы не можете удалить поездку, находящуюся в процессе выполнения";
	private static final String ROUTE_IS_IN_PROGRESS_OR_FINISHED =
			"Статус данного маршрута не позволяет вам начать поездку";
	private static final String ROUTE_IS_NOT_IN_PROGRESS = "Вы не можете закрыть поездку, которая еще не началась";
	private final RouteRepository routeRepo;
	private final RouteMapper routeMapper;

	@Transactional
	public OldAndNewRouteDto updateRouteById(RouteDto newVersion) {
		Route oldVersion = getRouteById(newVersion.getId());

		if(routeStatusIsWrong(newVersion,RouteStatus.FUTURE_ROUTE)){
			throw new WrongStatusException(WRONG_ROUTE_STATUS);
		}

		return new OldAndNewRouteDto()
				.setOldVersionRoute(oldVersion)
				.setNewVersionOfRoute(routeRepo.updateRoute(newVersion));
	}


	@Transactional
	public Page<RouteDto> getAllRoutes(RouteStatus routeStatus, String clientId, Long numberOfPage, Long elementsInPage) {
		return routeRepo.getRoutesByFilter(routeStatus,clientId,numberOfPage,elementsInPage);
	}

	@Transactional
	public RouteDto createNewRoute(RouteDto params) {
		return routeRepo.createNewRoute(params);
	}

	@Transactional
	public String deleteRouteById(Long routeId) {
		Route routeForDelete = routeRepo.getRouteById(routeId);
		checkRouteStatus(routeForDelete, Arrays.asList(RouteStatus.FUTURE_ROUTE,RouteStatus.ENDED_ROUTE));
		//toDo прописать работу с другими сущностями
		routeRepo.deleteRouteById(routeId);
		return SUCCESSFULLY_DELETE;
	}

	@Transactional
	public RouteDto getRouteById(Long routeId){
		return routeMapper.RouteToRouteDto(serviceRouteById(routeId));
	}


	private Route serviceRouteById(Long routeId) {
		return routeRepo.getRouteById(routeId);
	}


	@Transactional
	public RouteDto startRoute(Long id) {
		Route currentRoute = routeRepo.getRouteById(id);
		if(routeStatusIsWrong(currentRoute,RouteStatus.FUTURE_ROUTE)){
			throw new WrongStatusException(ROUTE_IS_IN_PROGRESS_OR_FINISHED);
		}
		return routeRepo.startRoute(id);
	}

	@Transactional
	public RouteDto finishRoute(Long id) {
		Route currentRoute = routeRepo.getRouteById(id);
		if(routeStatusIsWrong(currentRoute,RouteStatus.ROUTE_IN_PROCESS)){
			throw new WrongStatusException(ROUTE_IS_NOT_IN_PROGRESS);
		}
		return routeRepo.finishRoute(id);
	}

	private boolean routeStatusIsWrong(Route routeForCheck,RouteStatus successStatus) {
		return routeForCheck.getStatus()!= successStatus;
	}

	private void checkRouteStatus(Route routeForCheck, List<RouteStatus> successStatuses) {
		if(successStatuses.stream().anyMatch(status-> status==routeForCheck.getStatus())){
			throw new WrongStatusException(ROUTE_IS_IN_ACTION);
		}
	}
}
