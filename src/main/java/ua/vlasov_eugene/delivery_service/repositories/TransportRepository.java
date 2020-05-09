package ua.vlasov_eugene.delivery_service.repositories;

import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.ResultSetHandler;
import ua.vlasov_eugene.delivery_service.entities.Transport;
import ua.vlasov_eugene.delivery_service.enums.TransportStatus;

@Repository
public class TransportRepository {
	private static final String DISCONNECT_TRANSPORT_OF_CREW = "UPDATE transport SET crew_id = NULL WHERE crew_id=:crewId";
	private static final String GET_TRANSPORT_BY_ID = "SELECT * FROM transport WHERE id=:id LIMIT 1";
	private static final String CHANGE_STATUS = "UPDATE transport SET transport_status =:status WHERE id =:id";
	private ResultSetHandler<Transport> resultSetHandler = rs -> {
		Transport item = new Transport();
		item.setId(rs.getLong("id"));
		item.setStatus(TransportStatus.valueOf(rs.getString("transport_status")));
		item.setCrewId(rs.getLong("crew_id"));
		item.setRegistrationNumber(rs.getString("registration_number"));

		return item;
	};

	public void disconnectTransportOfCrew(Connection connection, Long id) {
		connection.createQuery(DISCONNECT_TRANSPORT_OF_CREW)
				.addParameter("crewId",id)
				.executeUpdate();
	}

	public void changeStatusById(Connection connection, Transport transport) {
		connection.createQuery(CHANGE_STATUS)
				.addParameter("status",transport.getStatus().name())
				.addParameter("id",transport.getId())
				.executeUpdate();
	}

	public Transport getTransportById(Connection connection, Long transportId) {
		return connection.createQuery(GET_TRANSPORT_BY_ID)
				.addParameter("id",transportId)
				.executeAndFetchFirst(resultSetHandler);
	}
}
