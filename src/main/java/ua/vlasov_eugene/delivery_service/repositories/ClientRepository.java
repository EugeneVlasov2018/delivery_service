package ua.vlasov_eugene.delivery_service.repositories;

import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.ResultSetHandler;
import ua.vlasov_eugene.delivery_service.entities.Client;

import java.util.List;


@Repository
public class ClientRepository {
	private static final String GET_CLIENTS_BY_ROUTE_ID = "SELECT c.* FROM client c " +
			"JOIN route_client rc ON c.id = rc.client_id " +
			"WHERE rc.route_id = :routeId";
	private ResultSetHandler<Client> resultSetHandler = rs -> {
		Client item = new Client();
		item.setId(rs.getLong("id"));
		item.setAddress(rs.getString("address"));
		item.setName(rs.getString("name"));
		item.setPhoneNumber(rs.getString("phone_number"));
		return item;
	};


	public List<Client> getClientsByRouteId(Connection connection, Long id) {
		return connection.createQuery(GET_CLIENTS_BY_ROUTE_ID)
				.addParameter("routeId",id)
				.executeAndFetch(resultSetHandler);
	}
}
