package ua.vlasov_eugene.delivery_service.repositories;

import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.ResultSetHandler;
import ua.vlasov_eugene.delivery_service.entities.Route;
import ua.vlasov_eugene.delivery_service.enums.RouteStatus;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

@Repository
public class RouteRepository {
	private static final String GET_ALL_ROUTES = "SELECT * FROM route LIMIT :limit OFFSET :offset";
	private static final String GET_ROUTE_BY_ID = "SELECT * FROM route WHERE id = :routeId LIMIT 1";
	private static final String STOP_ROUTE = "UPDATE route " +
			"SET end_route = :finish , route_status = :status " +
			"WHERE id=:routeId";
	private static final String START_ROUTE = "UPDATE route " +
			"SET start_route = :start , route_status = :status " +
			"WHERE id=:routeId";
	;
	private static final String ADD_NEW_ROUTE = "INSERT INTO route " +
			"(transport_id , route_status) " +
			"VALUES (:transportId , :routeStatus)";
	private static final String CONNECT_ROUTE_AND_CLIENT = "INSERT INTO route_client " +
			"VALUES (:routeId , :clientId)";

	private ResultSetHandler<Route> resultSetHandler = rs -> {
		Calendar tzUtc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

		Route item = new Route();
		item.setId(rs.getLong("id"));
		item.setTransportId(rs.getLong("transport_id"));
		item.setStart(rs.getTimestamp("start_route",tzUtc));
		item.setFinish(rs.getTimestamp("end_route",tzUtc));
		item.setStatus(RouteStatus.valueOf(rs.getString("route_status")));
		return item;
	};

	public Route getRouteById(Connection connection,Long id) {
		return connection.createQuery(GET_ROUTE_BY_ID)
				.addParameter("routeId", id)
				.executeAndFetchFirst(resultSetHandler);
	}

	public void startOrStopRoute(Connection connection, Route route) {
		if(route.getFinish()!=null){
			connection.createQuery(STOP_ROUTE)
					.addParameter("finish",
							LocalDateTime.ofInstant(route.getFinish().toInstant(), ZoneId.systemDefault()))
					.addParameter("status",route.getStatus().name())
					.addParameter("routeId",route.getId())
					.executeUpdate();
		} else {
			connection.createQuery(START_ROUTE)
					.addParameter("start",
							LocalDateTime.ofInstant(route.getStart().toInstant(),ZoneId.systemDefault()))
					.addParameter("status",route.getStatus().name())
					.addParameter("routeId",route.getId())
					.executeUpdate();
		}
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

	public void add(Connection connection, Route route) {
		Long id = connection.createQuery(ADD_NEW_ROUTE)
				.addParameter("transportId", route.getTransportId())
				.addParameter("routeStatus", RouteStatus.FUTURE_ROUTE.name())
				.executeUpdate()
				.getKey(Long.class);

		route.setId(id);
	}

	public void connectRouteAndClient(Connection connection, Long routeId, Long clientId) {
		connection.createQuery(CONNECT_ROUTE_AND_CLIENT)
				.addParameter("clientId",clientId)
				.addParameter("routeId",routeId)
				.executeUpdate();
	}
}
