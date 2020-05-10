package ua.vlasov_eugene.delivery_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import ua.vlasov_eugene.delivery_service.custom_validator.EntityValidator;
import ua.vlasov_eugene.delivery_service.entities.Client;
import ua.vlasov_eugene.delivery_service.entities.Route;
import ua.vlasov_eugene.delivery_service.entities.Transport;
import ua.vlasov_eugene.delivery_service.entities.VehicleCrew;
import ua.vlasov_eugene.delivery_service.enums.CrewStatus;
import ua.vlasov_eugene.delivery_service.enums.RouteStatus;
import ua.vlasov_eugene.delivery_service.enums.TransportStatus;
import ua.vlasov_eugene.delivery_service.dtos.RouteDto;
import ua.vlasov_eugene.delivery_service.exceptions.WrongParameterException;
import ua.vlasov_eugene.delivery_service.repositories.ClientRepository;
import ua.vlasov_eugene.delivery_service.repositories.CrewRepository;
import ua.vlasov_eugene.delivery_service.repositories.RouteRepository;
import ua.vlasov_eugene.delivery_service.repositories.TransportRepository;
import ua.vlasov_eugene.delivery_service.utils.Page;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteService {
	private static final String ROUTE_NOT_EXIST = "Поездки с таким id не не существует";
	private final RouteRepository routeRepo;
	private final ClientRepository clientRepo;
	private final CrewRepository crewRepo;
	private final TransportRepository transportRepo;
	private final EntityValidator validator;

	private final Sql2o sql2o;

	@Transactional
	public Page<RouteDto> getAllRoutes(Long numberOfPage, Long elementsInPage) {
		Page<RouteDto> result;
		try (Connection connection = sql2o.beginTransaction()) {
			List<Route> routes;
			routes = routeRepo.getAllRoutes(connection, numberOfPage - 1, elementsInPage);
			result = preparePage(connection, routes);
			connection.commit();
		}
		return result;
	}

	@Transactional
	public RouteDto createNewRoute(Long transportId,Long crewId, List<Long> clientId) {
		RouteDto result;
		Route newRoute = new Route().setStatus(RouteStatus.FUTURE_ROUTE);
		try (Connection connection = sql2o.beginTransaction()) {
			Transport transport = transportRepo.getTransportById(connection, transportId);
			validator.checkTransportStatus(transport, TransportStatus.FREE);

			VehicleCrew crew = crewRepo.getCrewById(connection, crewId);
			validator.crewIsOnRide(crew, CrewStatus.READY_FOR_RIDE);

			List<Client> clients = getValidClientsById(connection, clientId);

			transport.setCrewId(crewId);
			newRoute.setTransportId(transportId);
			routeRepo.add(connection, newRoute);

			connectCLientsAndRoute(connection, newRoute.getId(), clientId);

			result = prepareRouteForResult(newRoute, transport, clients);
			connection.commit();
		}

		return result;
	}


	@Transactional
	public RouteDto getRouteById(Long routeId){
		RouteDto result;
		try(Connection connection = sql2o.beginTransaction()) {
			Route routeFromDb = routeRepo.getRouteById(connection, routeId);

			if (!validator.routeIsExist(routeFromDb)) {
				throw new WrongParameterException(ROUTE_NOT_EXIST);
			}

			result = prepareRouteFromDbForResult(connection, routeFromDb);
			connection.commit();
		}
		return result;
	}

	@Transactional
	public RouteDto startRoute(Long id) {
		RouteDto result = new RouteDto();
		try (Connection connection = sql2o.beginTransaction()) {
			Route currentRoute = routeRepo.getRouteById(connection, id);
			validator.checkRouteStatus(currentRoute, RouteStatus.FUTURE_ROUTE);

			Transport transport = transportRepo.getTransportById(connection, currentRoute.getTransportId());
			validator.checkTransportStatus(transport, TransportStatus.FREE);

			VehicleCrew crew = crewRepo.getCrewById(connection, transport.getCrewId());
			validator.crewIsOnRide(crew, CrewStatus.READY_FOR_RIDE);

			currentRoute.setStart(new Date());
			currentRoute.setStatus(RouteStatus.ROUTE_IN_PROCESS);
			transport.setStatus(TransportStatus.ON_THE_ROAD);
			crew.setStatus(CrewStatus.ON_RIDE);
			routeRepo.startOrStopRoute(connection, currentRoute);
			transportRepo.changeStatusById(connection, transport);
			crewRepo.changeStatusById(connection, crew);

			result.setId(currentRoute.getId())
					.setStatus(currentRoute.getStatus())
					.setTransport(transport)
					.setStart(currentRoute.getStart())
					.setEnd(currentRoute.getFinish());

			connection.commit();
		}
		return result;
	}

	@Transactional
	public RouteDto finishRoute(Long id) {
		RouteDto result = new RouteDto();
		try(Connection connection = sql2o.beginTransaction()) {
			Route currentRoute = routeRepo.getRouteById(connection, id);
			validator.checkRouteStatus(currentRoute, RouteStatus.ROUTE_IN_PROCESS);

			Transport transport = transportRepo.getTransportById(connection, currentRoute.getTransportId());
			validator.checkTransportStatus(transport, TransportStatus.ON_THE_ROAD);

			VehicleCrew crew = crewRepo.getCrewById(connection, transport.getCrewId());
			validator.crewIsOnRide(crew, CrewStatus.ON_RIDE);

			currentRoute.setFinish(new Date());
			currentRoute.setStatus(RouteStatus.ENDED_ROUTE);
			transport.setStatus(TransportStatus.FREE);
			crew.setStatus(CrewStatus.READY_FOR_RIDE);

			routeRepo.startOrStopRoute(connection, currentRoute);
			transportRepo.changeStatusById(connection, transport);
			crewRepo.changeStatusById(connection, crew);

			result.setId(currentRoute.getId())
					.setStatus(currentRoute.getStatus())
					.setTransport(transport)
					.setStart(currentRoute.getStart())
					.setEnd(currentRoute.getFinish());

			connection.commit();
		}

		return result;
	}

	private Page<RouteDto> preparePage(Connection connection, List<Route> routes) {
		List<RouteDto> result = routes.stream()
				.map(route -> prepareRouteFromDbForResult(connection,route))
				.collect(Collectors.toList());

		return new Page<RouteDto>().setItems(result).setTotalCount(result.size());
	}

	private RouteDto prepareRouteFromDbForResult(Connection connection, Route routeFromDb) {
		RouteDto result = new RouteDto()
				.setId(routeFromDb.getId())
				.setStart(routeFromDb.getStart())
				.setEnd(routeFromDb.getFinish())
				.setStatus(routeFromDb.getStatus())
				.setRouteClient(clientRepo.getClientsByRouteId(
						connection, routeFromDb.getId()));

		if (routeFromDb.getTransportId() != null) {
			result.setTransport(
					transportRepo.getTransportById(
							connection, routeFromDb.getTransportId()));
		}
		return result;
	}

	private RouteDto prepareRouteForResult(Route newRoute, Transport transport, List<Client> clients) {
		return new RouteDto()
				.setId(newRoute.getId())
				.setStart(newRoute.getStart())
				.setEnd(newRoute.getFinish())
				.setStatus(newRoute.getStatus())
				.setTransport(transport)
				.setRouteClient(clients);
	}

	private void connectCLientsAndRoute(Connection connection, Long routeId, List<Long> clientsId) {
		for(Long clientId : clientsId){
			routeRepo.connectRouteAndClient(connection,routeId,clientId);
		}
	}

	private List<Client> getValidClientsById(Connection connection, List<Long> clientId) {
		List<Client> result = clientId.stream()
				.map(id -> clientRepo.getClientsById(connection, id))
				.collect(Collectors.toList());

		validator.checkAllClientsOnValid(result);

		return result;
	}
}
