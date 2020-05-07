package ua.vlasov_eugene.delivery_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.vlasov_eugene.delivery_service.dtos.OldAndNewRouteDto;
import ua.vlasov_eugene.delivery_service.entityes.Route;
import ua.vlasov_eugene.delivery_service.enums.RouteStatus;
import ua.vlasov_eugene.delivery_service.repositories.RouteRepository;
import ua.vlasov_eugene.delivery_service.utils.Page;

@Service
@RequiredArgsConstructor
public class RouteService {
	private final RouteRepository RouteRepo;

	@Transactional
	public OldAndNewRouteDto updateRouteById(Route route) {
		return null;
	}

	@Transactional
	public Page<Route> getAllRoutes(RouteStatus routeStatus, String clientId, Long numberOfPage, Long elementsInPage) {
		return null;
	}

	@Transactional
	public Route createNewRoute(Route params) {
		return null;
	}

	@Transactional
	public String deleteRouteById(Long routeId) {
		return null;
	}

	@Transactional
	public Route getRouteById(Long routeId) {
		return null;
	}

	@Transactional
	public Route startRoute(Long id) {
		return null;
	}

	@Transactional
	public Route finishRoute(Long id) {
		return null;
	}
}
