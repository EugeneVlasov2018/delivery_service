package ua.vlasov_eugene.delivery_service.repositories;

import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.ResultSetHandler;
import ua.vlasov_eugene.delivery_service.entities.Transport;
import ua.vlasov_eugene.delivery_service.enums.TransportStatus;

@Repository
public class TransportRepository {
	private static final String DISCONNECT_TRANSPORT_OF_CREW = "UPDATE transport SET crew_id = NULL WHERE crew_id=:crewId";
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
}
