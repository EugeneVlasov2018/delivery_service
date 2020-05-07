package ua.vlasov_eugene.delivery_service.repositories;

import org.springframework.stereotype.Repository;
import ua.vlasov_eugene.delivery_service.entityes.Route;
import ua.vlasov_eugene.delivery_service.enums.RouteStatus;
import ua.vlasov_eugene.delivery_service.utils.Page;

@Repository
public class RouteRepository {
	public Route getRouteById(Long id) {
		return null;
	}

	public Route updateRoute(Route route) {
		return null;
	}

	public Page<Route> getRoutesByFilter(RouteStatus routeStatus, String clientId, Long numberOfPage, Long elementsInPage) {
		return null;
	}

	public Route createNewRoute(Route params) {
		return null;
	}

	public void deleteRouteById(Long routeId) {

	}

	public Route startRoute(Long id) {
		return null;
	}

	public Route finishRoute(Long id) {
		return null;
	}
}
