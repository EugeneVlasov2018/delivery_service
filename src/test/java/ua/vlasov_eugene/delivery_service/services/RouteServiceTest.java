package ua.vlasov_eugene.delivery_service.services;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import ua.vlasov_eugene.delivery_service.custom_validator.EntityValidator;
import ua.vlasov_eugene.delivery_service.dtos.RouteDto;
import ua.vlasov_eugene.delivery_service.entities.Route;
import ua.vlasov_eugene.delivery_service.entities.Transport;
import ua.vlasov_eugene.delivery_service.enums.RouteStatus;
import ua.vlasov_eugene.delivery_service.enums.TransportStatus;
import ua.vlasov_eugene.delivery_service.exceptions.WrongParameterException;
import ua.vlasov_eugene.delivery_service.repositories.ClientRepository;
import ua.vlasov_eugene.delivery_service.repositories.CrewRepository;
import ua.vlasov_eugene.delivery_service.repositories.RouteRepository;
import ua.vlasov_eugene.delivery_service.repositories.TransportRepository;

import java.util.Collections;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class RouteServiceTest {
	private Long innedId;
	private Transport transportFromDB;
	private RouteDto expectedDto;
	private Route routeFromDb;
	@Mock
	private Sql2o sql2o;
	@Mock
	private Connection connection;
	@Mock
	private RouteRepository routeRepo;
	@Mock
	private ClientRepository clientRepo;
	@Mock
	private CrewRepository crewRepo;
	@Mock
	private TransportRepository transportRepo;
	@Mock
	private EntityValidator validator;
	@InjectMocks
	private RouteService testedClass;

	@Before
	public void prepare() {
		innedId = 1L;

		transportFromDB = prepareTransport();
		routeFromDb = prepareRoute();
		expectedDto = prepareDto();

		when(sql2o.beginTransaction()).thenReturn(connection);
		when(routeRepo.getRouteById(connection, innedId)).thenReturn(routeFromDb);
		when(validator.routeIsExist(routeFromDb)).thenReturn(true);
		when(transportRepo.getTransportById(connection, routeFromDb.getId()))
				.thenReturn(transportFromDB);
	}


	@Test(expected = WrongParameterException.class)
	public void getRouteByIdExpectedExceptionBecauseIdIsWrong() {
		when(routeRepo.getRouteById(connection, innedId)).thenReturn(null);
		when(validator.routeIsExist(null)).thenReturn(false);

		testedClass.getRouteById(innedId);

		verify(sql2o).beginTransaction();
		verify(routeRepo).getRouteById(connection, innedId);
		verify(validator).routeIsExist(null);
	}

	@Test
	public void getRouteFromDbByIdExpectedSuccessWithNullableTransport() {
		assertEquals(expectedDto, testedClass.getRouteById(innedId));
	}

	@Test
	public void getRouteFromDBByIdExpectedSuccessWithTransport() {
		expectedDto.setTransport(transportFromDB);
		routeFromDb.setTransportId(1L);
		assertEquals(expectedDto, testedClass.getRouteById(innedId));
	}

	private Transport prepareTransport() {
		return new Transport()
				.setId(1L)
				.setStatus(TransportStatus.FREE)
				.setCrewId(1L)
				.setRegistrationNumber("SOME NUMBER");
	}

	private RouteDto prepareDto() {
		return new RouteDto()
				.setId(innedId)
				.setStatus(RouteStatus.FUTURE_ROUTE)
				.setRouteClient(Collections.emptyList());
	}

	private Route prepareRoute() {
		return new Route()
				.setId(innedId)
				.setStatus(RouteStatus.FUTURE_ROUTE);
	}
}
