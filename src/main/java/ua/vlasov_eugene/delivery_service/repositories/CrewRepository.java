package ua.vlasov_eugene.delivery_service.repositories;

import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.ResultSetHandler;
import ua.vlasov_eugene.delivery_service.dtos.CrewDto;
import ua.vlasov_eugene.delivery_service.entities.VehicleCrew;
import ua.vlasov_eugene.delivery_service.enums.CrewStatus;

import java.util.List;

@Repository
public class CrewRepository {
	private static final String GET_ALL_CREW = "SELECT * FROM vehicle_crew LIMIT :limit OFFSET :offset";
	private static final String GET_ALL_CREW_WITH_STATUS = "SELECT * FROM vehicle_crew WHERE crew_status =:status LIMIT :limit OFFSET :offset";
	private static final String GET_CREW_BY_ID = "SELECT * FROM vehicle_crew WHERE id =:crewId LIMIT 1";
	private static final String UPDATE_CREW_STATUS = "UPDATE vehicle_crew SET crew_status=:status WHERE id=:crewId";
	private static final String DELETE_CREW = "DELETE FROM vehicle_crew WHERE id = :crewId";
	private static final String ADD_NEW_CREW = "INSERT INTO vehicle_crew (crew_status) VALUES (:status)";

	private ResultSetHandler<VehicleCrew> resultSetHandler = rs -> {
		VehicleCrew item = new VehicleCrew();
		item.setId(rs.getLong("id"));
		item.setStatus(CrewStatus.valueOf(rs.getString("crew_status")));
		return item;
	};

	public List<VehicleCrew> getAllCrews(Connection connection, Long numberOfPage, Long elementsInPage) {
		long offset = 0;
		if (elementsInPage!=null)
			offset = elementsInPage*numberOfPage;
		return connection.createQuery(GET_ALL_CREW)
				.addParameter("limit", elementsInPage)
				.addParameter("offset", offset)
				.executeAndFetch(resultSetHandler);
	}

	public List<VehicleCrew> getAllCrewsWithCurrentStatus(Connection connection,
													  CrewStatus crewStatus,
													  Long numberOfPage,
													  Long elementsInPage) {
		long offset = 0;
		if (elementsInPage!=null)
			offset = elementsInPage*numberOfPage;
		return connection.createQuery(GET_ALL_CREW_WITH_STATUS)
				.addParameter("status",crewStatus.name())
				.addParameter("limit", elementsInPage)
				.addParameter("offset", offset)
				.executeAndFetch(resultSetHandler);
	}

	public VehicleCrew getCrewById(Connection connection,Long id) {
		return connection.createQuery(GET_CREW_BY_ID)
				.addParameter("crewId", id)
				.executeAndFetchFirst(resultSetHandler);
	}

	public void updateCrewStatus(Connection connection, CrewDto newVersion) {
		connection.createQuery(UPDATE_CREW_STATUS)
				.addParameter("status",newVersion.getStatus().name())
				.addParameter("crewId",newVersion.getId())
				.executeUpdate();
	}

	public void deleteCrewById(Connection connection,Long id) {
		connection.createQuery(DELETE_CREW)
				.addParameter("crewId",id)
				.executeUpdate();
	}

	public void addNewCrew(Connection connection, VehicleCrew newCrew) {
		Long id = connection.createQuery(ADD_NEW_CREW)
				.bind(newCrew)
				.executeUpdate()
				.getKey(Long.class);
		
		newCrew.setId(id);
	}
}
