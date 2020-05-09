package ua.vlasov_eugene.delivery_service.repositories;

import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.ResultSetHandler;
import ua.vlasov_eugene.delivery_service.entities.Route;
import ua.vlasov_eugene.delivery_service.enums.RouteStatus;
import ua.vlasov_eugene.delivery_service.utils.Page;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

@Repository
public class RouteRepository {
	private static final String GET_ALL_ROUTES = "SELECT * FROM route LIMIT :limit OFFSET :offset";
	private static final String GET_ROUTE_BY_ID = "SELECT * FROM ROUTE WHERE id=:routeId LIMIT 1";
	private static final String STOP_ROUTE = "UPDATE route " +
			"SET end_route = :finish , route_status = :status " +
			"WHERE id=:routeId";
	private static final String START_ROUTE = "UPDATE route " +
			"SET start_route = :start , route_status = :status " +
			"WHERE id=:routeId";;

	private ResultSetHandler<Route> resultSetHandler = rs -> {
		Calendar tzUtc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

		Route item = new Route();
		item.setId(rs.getLong("id"));
		item.setTransportId(rs.getLong("transport_id"));
		item.setStart(rs.getTimestamp("start_route",tzUtc)));
		item.setFinish(rs.getTimestamp("end_route",tzUtc));
		item.setStatus(RouteStatus.valueOf(rs.getString("route_status"))))
		return item;
	};

	public Route getRouteById(Connection connection,Long id) {
		return connection.createQuery(GET_ROUTE_BY_ID)
				.addParameter("routeId", id)
				.executeAndFetchFirst(resultSetHandler);
	}

	public void startOrStopRoute(Connection connection, Route route) {
		if(route.getFinish()==null){
			connection.createQuery(STOP_ROUTE)
					.addParameter("finish",route.getFinish())
					.addParameter("status",route.getStatus().name())
					.addParameter("routeId",route.getId())
					.executeUpdate();
		} else {
			connection.createQuery(START_ROUTE)
					.addParameter("start",route.getStart())
					.addParameter("status",route.getStatus().name())
					.addParameter("routeId",route.getId())
					.executeUpdate();
		}
		
	}

	public Page<Route> getRoutesByFilter(Long clientId, Long numberOfPage, Long elementsInPage) {
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

	public List<Route> getAllRoutes(Connection connection, Long numberOfPage, Long elementsInPage) {
		long offset = 0;
		if (elementsInPage!=null)
			offset = elementsInPage*numberOfPage;
		return connection.createQuery(GET_ALL_ROUTES)
				.addParameter("limit", elementsInPage)
				.addParameter("offset", offset)
				.executeAndFetch(resultSetHandler);
	}
}
