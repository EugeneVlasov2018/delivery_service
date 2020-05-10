package ua.vlasov_eugene.delivery_service.custom_validator;

import org.springframework.stereotype.Component;
import ua.vlasov_eugene.delivery_service.entities.*;
import ua.vlasov_eugene.delivery_service.enums.CrewStatus;
import ua.vlasov_eugene.delivery_service.enums.RouteStatus;
import ua.vlasov_eugene.delivery_service.enums.TransportStatus;
import ua.vlasov_eugene.delivery_service.exceptions.WrongParameterException;

import java.util.List;
import java.util.Objects;

@Component
public class EntityValidator {
	private static final String WRONG_STATUS_OF_CREW = "Вы не можете менять экипаж, находящийся в поездке";
	private static final String COURIER_NOT_VALID = "Курьер без ID - не валиден";
	private static final String ROUTE_NOT_EXIST = "Поездки с таким id не не существует";
	private static final String WRONG_STATUS_FOR_START = "Текущий статус поездки" +
			" не позволяет вам ее начать или закончить";
	private static final String WRONG_TRANSPORT_STATUS = "Текущий статус ТС не позволяет вам" +
			"начать или завершить поездку";
	private static final String CREW_IS_NOT_READY = "Экипаж не готов к таким подвигам";
	private static final String TRANSPORT_NOT_EXIST = "Транспорта с таким id не существует";
	private static final String CREW_IS_NOT_EXIST = "Экипажа с таким id не существует";
	private static final String CLIENT_IS_NOT_EXIST = "Клиент из списка отсутствует базе данных";

	//TODO в идеале, лучше все методы перевести в boolean (удобнее тестить, мокая) нона рефакторинг не хватило времени
	public void crewIsOnRide(VehicleCrew crew, CrewStatus successStatus) {
		if (crew == null) {
			throw new WrongParameterException(CREW_IS_NOT_EXIST);
		}
		if (crew.getStatus() != successStatus) {
			throw new WrongParameterException(CREW_IS_NOT_READY);
		}
	}

	public void checkTransportStatus(Transport transport, TransportStatus successStatus) {
		if (transport == null) {
			throw new WrongParameterException(TRANSPORT_NOT_EXIST);
		}
		if (transport.getStatus() != successStatus) {
			throw new WrongParameterException(WRONG_TRANSPORT_STATUS);
		}
	}

	public void checkRouteStatus(Route currentRoute, RouteStatus successStatus) {
		if (currentRoute == null) {
			throw new WrongParameterException(ROUTE_NOT_EXIST);
		}
		if (currentRoute.getStatus() != successStatus) {
			throw new WrongParameterException(WRONG_STATUS_FOR_START);
		}
	}

	public boolean routeIsExist(Route currentRoute) {
		return currentRoute != null;
	}

	public void checkAllClientsOnValid(List<Client> result) {
		boolean allClientsIsNotValid = result.stream()
				.anyMatch(Objects::isNull);
		if (allClientsIsNotValid) {
			throw new WrongParameterException(CLIENT_IS_NOT_EXIST);
		}
	}

	public void checkCouriersIsExist(List<Courier> couriers) {
		for (Courier courier : couriers) {
			if (courier.getId() == null || courier.getId() == 0L) {
				throw new WrongParameterException(COURIER_NOT_VALID);
			}
		}
	}

	public void checkCrewStatus(VehicleCrew oldVersion,
								CrewStatus sameStatus,
								CrewStatus exceptionStatus) {
		if (oldVersion.getStatus() == sameStatus) {
			return;
		}

		if (oldVersion.getStatus() == exceptionStatus) {
			throw new WrongParameterException(WRONG_STATUS_OF_CREW);
		}
	}

	public boolean crewIsOnRide(VehicleCrew currentCrew) {
		return currentCrew.getStatus() == CrewStatus.ON_RIDE;
	}
}
