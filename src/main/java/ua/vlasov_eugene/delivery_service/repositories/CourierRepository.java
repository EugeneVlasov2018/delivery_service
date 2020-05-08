package ua.vlasov_eugene.delivery_service.repositories;

import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.ResultSetHandler;
import ua.vlasov_eugene.delivery_service.entities.Courier;

import java.util.List;

@Repository
public class CourierRepository {
	private static final String GET_COURIERS_BY_ID = "SELECT * FROM courier WHERE crew_id=:crewId";
	private static final String COURIER_IS_BYSY = "SELECT * FROM courier " +
			"WHERE id=:courierId AND crew_id IS NULL OR id=:courierId AND crew_id !=:crewId";
	private static final String DISCONNECT_ALL_COURIERS_FROM_CREW = "UPDATE courier SET crew_id = NULL WHERE crew_id =:crewId";
	private static final String ADD_COURIER_TO_CREW = "UPDATE courier SET crew_id = :crewId WHERE id =:courierId";
	private static final String GET_COURIER_BY_ID = "SELECT * FROM courier WHERE id=:id LIMIT 1";

	private ResultSetHandler<Courier> resultSetHandler = rs -> {
		Courier item = new Courier();
		item.setId(rs.getLong("id"));
		item.setFirstNameLastName(rs.getString("firstname_lastname"));
		item.setPhoneNumber(rs.getString("phone_number"));
		item.setCrewId(rs.getLong("crew_id"));
		return item;
	};

	public List<Courier> getCouriersByCrewId(Connection connection, Long id) {
		return connection.createQuery(GET_COURIERS_BY_ID)
				.addParameter("crewId", id)
				.executeAndFetch(resultSetHandler);
	}

	public boolean checkCouriersStatus(Connection connection, Long courierId, Long crewId) {
		Courier courier = connection.createQuery(COURIER_IS_BYSY)
				.addParameter("courierId",courierId)
				.addParameter("crewId",crewId)
				.executeAndFetchFirst(resultSetHandler);

		return courier==null;
	}

	public void disconnectAllOldCouriers(Connection connection, Long id) {
		connection.createQuery(DISCONNECT_ALL_COURIERS_FROM_CREW)
				.addParameter("crewId",id)
				.executeUpdate();
	}

	public void addCouriersToCrew(Connection connection, Long crewId, Long courierId) {
		connection.createQuery(ADD_COURIER_TO_CREW)
				.addParameter("courierId",courierId)
				.addParameter("crewId",crewId)
				.executeUpdate();
	}

	public Courier getCourierById(Connection connection, Long id) {
		return connection.createQuery(GET_COURIER_BY_ID)
				.addParameter("id", id)
				.executeAndFetchFirst(resultSetHandler);
	}
}
