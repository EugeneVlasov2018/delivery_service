package ua.vlasov_eugene.delivery_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import ua.vlasov_eugene.delivery_service.entities.Client;
import ua.vlasov_eugene.delivery_service.entities.Route;
import ua.vlasov_eugene.delivery_service.entities.Transport;
import ua.vlasov_eugene.delivery_service.entities.VehicleCrew;
import ua.vlasov_eugene.delivery_service.enums.CrewStatus;
import ua.vlasov_eugene.delivery_service.enums.RouteStatus;
import ua.vlasov_eugene.delivery_service.enums.TransportStatus;
import ua.vlasov_eugene.delivery_service.exceptions.WrongParameterException;
import ua.vlasov_eugene.delivery_service.dtos.RouteDto;
import ua.vlasov_eugene.delivery_service.repositories.ClientRepository;
import ua.vlasov_eugene.delivery_service.repositories.CrewRepository;
import ua.vlasov_eugene.delivery_service.repositories.RouteRepository;
import ua.vlasov_eugene.delivery_service.repositories.TransportRepository;
import ua.vlasov_eugene.delivery_service.utils.Page;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteService {
	private static final String ROUTE_NOT_EXIST = "Поездки с таким id не не существует";
	private static final String WRONG_STATUS_FOR_START = "Текущий статус поездки" +
			" не позволяет вам ее начать или закончить";
	private static final String WRONG_TRANSPORT_STATUS = "Текущий статус ТС не позволяет вам" +
			"начать или завершить поездку";
	private static final String CREW_IS_NOT_READY = "Экипаж не готов к таким подвигам";
	private static final String TRANSPORT_NOT_EXIST = "Транспорта с таким id не существует";
	private static final String CREW_IS_NOT_EXIST = "Экипажа с таким id не существует";
	private static final String CLIENT_IS_NOT_EXIST = "Клиент из списка отсутствует базе данных";
	private final RouteRepository routeRepo;
	private final ClientRepository clientRepo;
	private final CrewRepository crewRepo;
	private final TransportRepository transportRepo;

	private final Sql2o sql2o;

	@Transactional
	public Page<RouteDto> getAllRoutes(Long numberOfPage, Long elementsInPage) {
		Page<RouteDto> result;
		try (Connection connection = sql2o.beginTransaction()){
			List<Route> routes;
			routes = routeRepo.getAllRoutes(connection,numberOfPage-1,elementsInPage);
			result = preparePage(connection,routes);
			connection.commit();
		}
		return result;
	}

	@Transactional
	public RouteDto createNewRoute(Long transportId,Long crewId, List<Long> clientId) {
		RouteDto result;
		Route newRoute = new Route().setStatus(RouteStatus.FUTURE_ROUTE);
		try (Connection connection = sql2o.beginTransaction()){
			Transport transport = transportRepo.getTransportById(connection,transportId);
			checkTransportStatus(transport, TransportStatus.FREE);

			VehicleCrew crew = crewRepo.getCrewById(connection,crewId);
			checkCrewStatus(crew,CrewStatus.READY_FOR_RIDE);

			List<Client> clients = getValidClientsById(connection,clientId);

			transport.setCrewId(crewId);
			newRoute.setTransportId(transportId);
			routeRepo.add(connection,newRoute);

			connectCLientsAndRoute(connection,newRoute.getId(),clientId);

			result = prepareRouteForResult(newRoute,transport,clients);
			connection.commit();
		}

		return result;
	}


	@Transactional
	public RouteDto getRouteById(Long routeId){
		if(routeId==null||routeId==0){
			throw new WrongParameterException(ROUTE_NOT_EXIST);
		}
		RouteDto result;
		try(Connection connection = sql2o.beginTransaction()){
			Route routeFromDb = routeRepo.getRouteById(connection,routeId);
			checkRouteOfNullable(routeFromDb);

			result = prepareRouteFromDbForResult(connection,routeFromDb);
			connection.commit();
		}
		return result;
	}

	@Transactional
	public RouteDto startRoute(Long id) {
		RouteDto result = new RouteDto();
		try (Connection connection = sql2o.beginTransaction()) {
			Route currentRoute = routeRepo.getRouteById(connection, id);
			checkRouteStatus(currentRoute, RouteStatus.FUTURE_ROUTE);

			Transport transport = transportRepo.getTransportById(connection, currentRoute.getTransportId());
			checkTransportStatus(transport, TransportStatus.FREE);

			VehicleCrew crew = crewRepo.getCrewById(connection, transport.getCrewId());
			checkCrewStatus(crew, CrewStatus.READY_FOR_RIDE);

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
			System.out.println(currentRoute.getStart().getTime());
			checkRouteStatus(currentRoute, RouteStatus.ROUTE_IN_PROCESS);

			Transport transport = transportRepo.getTransportById(connection, currentRoute.getTransportId());
			checkTransportStatus(transport, TransportStatus.ON_THE_ROAD);

			VehicleCrew crew = crewRepo.getCrewById(connection, transport.getCrewId());
			checkCrewStatus(crew, CrewStatus.ON_RIDE);

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

	private void checkCrewStatus(VehicleCrew crew, CrewStatus successStatus) {
		if(crew==null){
			throw new WrongParameterException(CREW_IS_NOT_EXIST);
		}
		if(crew.getStatus()!=successStatus){
			throw new WrongParameterException(CREW_IS_NOT_READY);
		}
	}

	private void checkTransportStatus(Transport transport, TransportStatus successStatus) {
		if(transport==null){
			throw new WrongParameterException(TRANSPORT_NOT_EXIST);
		}
		if(transport.getStatus()!=successStatus){
			throw new WrongParameterException(WRONG_TRANSPORT_STATUS);
		}
	}

	private void checkRouteStatus(Route currentRoute, RouteStatus successStatus) {
		if(currentRoute==null){
			throw new WrongParameterException(ROUTE_NOT_EXIST);
		}
		if(currentRoute.getStatus()!=successStatus){
			throw new WrongParameterException(WRONG_STATUS_FOR_START);
		}
	}

	private void checkRouteOfNullable(Route currentRoute) {
		if(currentRoute==null){
			throw new WrongParameterException(ROUTE_NOT_EXIST);
		}
	}

	private Page<RouteDto> preparePage(Connection connection, List<Route> routes) {
		List<RouteDto> result = routes.stream()
				.map(route -> prepareRouteFromDbForResult(connection,route))
				.collect(Collectors.toList());

		return new Page<RouteDto>().setItems(result).setTotalCount(result.size());
	}

	private RouteDto prepareRouteFromDbForResult(Connection connection, Route routeFromDb) {
		Transport transport = transportRepo.getTransportById(connection,routeFromDb.getTransportId());
		List<Client> clients = clientRepo.getClientsByRouteId(connection,routeFromDb.getId());
		return new RouteDto()
				.setId(routeFromDb.getId())
				.setStart(routeFromDb.getStart())
				.setEnd(routeFromDb.getFinish())
				.setStatus(routeFromDb.getStatus())
				.setRouteClient(clients)
				.setTransport(transport);
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
				.map(id -> clientRepo.getClientsById(connection,id))
				.collect(Collectors.toList());

		boolean allClientsIsNotValid = result.stream()
				.anyMatch(Objects::isNull);

		if(allClientsIsNotValid){
			throw new WrongParameterException(CLIENT_IS_NOT_EXIST);
		}

		return result;
	}
}
