package ua.vlasov_eugene.delivery_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import ua.vlasov_eugene.delivery_service.dtos.CrewDto;
import ua.vlasov_eugene.delivery_service.entities.Courier;
import ua.vlasov_eugene.delivery_service.entities.VehicleCrew;
import ua.vlasov_eugene.delivery_service.enums.CrewStatus;
import ua.vlasov_eugene.delivery_service.exceptions.WrongCourierException;
import ua.vlasov_eugene.delivery_service.exceptions.WrongCrewException;
import ua.vlasov_eugene.delivery_service.exceptions.WrongParameterException;
import ua.vlasov_eugene.delivery_service.repositories.CourierRepository;
import ua.vlasov_eugene.delivery_service.repositories.CrewRepository;
import ua.vlasov_eugene.delivery_service.repositories.TransportRepository;
import ua.vlasov_eugene.delivery_service.utils.Page;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CrewService {
	private static final String WRONG_STATUS_OF_CREW = "Вы не можете менять экипаж, находящийся в поездке";
	private static final String COURIER_IS_BUSY = "Вы не можете добавить в экипаж курьера, уже находящегося в другом экипаже";
	private static final String SUCCESSFULLY_DELETE = "Данные о экипаже были успешно удалены";
	private static final String COURIER_NOT_VALID = "Курьер без ID - не валиден";
	private static final String CREW_NOT_EXIST = "Экипажа с таким ID не существует";
	private final CrewRepository crewRepo;
	private final CourierRepository courierRepo;
	private final TransportRepository transportRepo;
	private final Sql2o sql2o;

	@Transactional
	public Page<CrewDto> getCrewByFilter(Long numberOfPage, Long elementsInPage) {
		Page<CrewDto> result;
		try(Connection connection = sql2o.beginTransaction()) {
			List<VehicleCrew> crews =  crewRepo.getAllCrews(connection,
					numberOfPage-1, elementsInPage);
			result = createPageFromCrews(connection, crews);
			connection.commit();
		}

		return result;
	}

	@Transactional
	public CrewDto getCrewById(Long id) throws SQLException {
		CrewDto result;
		try(Connection connection = sql2o.beginTransaction()){
			VehicleCrew crewFromDB = crewRepo.getCrewById(connection,id);
			result = createCrew(connection,crewFromDB);
			connection.commit();
		}
		return result;
	}

	@Transactional
	public CrewDto updateCrew(CrewDto newVersionOfCrew) {
		CrewDto result = new CrewDto();
		try(Connection connection = sql2o.beginTransaction()){
			VehicleCrew oldCrew = crewRepo.getCrewById(connection,newVersionOfCrew.getId());

			checkCrewStatus(oldCrew,newVersionOfCrew.getStatus());
			checkAllCouriersFromNewVersion(connection,
					newVersionOfCrew.getCouriers(),
					oldCrew.getId());

			courierRepo.disconnectAllOldCouriers(connection,newVersionOfCrew.getId());
			addNewCouriersToCrew(connection,newVersionOfCrew.getId(),newVersionOfCrew.getCouriers());
			crewRepo.updateCrewStatus(connection,newVersionOfCrew);

			//TODO этот блок, в принципе, не обязателен (если производительность критична, -
			// можно убивать, просто вернув newVersionOfCrew),
			// но он гарантирует, чтов БД все прошло гуд
			VehicleCrew newCrew =  crewRepo.getCrewById(connection,newVersionOfCrew.getId());
			List<Courier> newCouriers = courierRepo.getCouriersByCrewId(connection,newCrew.getId());

			result.setId(newCrew.getId()).setStatus(newCrew.getStatus()).setCouriers(newCouriers);

			connection.commit();
		}

		return result;
	}

	@Transactional
	public String deleteById(Long id) {
		try(Connection connection = sql2o.beginTransaction()){
			VehicleCrew currentCrew = crewRepo.getCrewById(connection,id);
			checkCrewOnNullable(currentCrew);

			checkCrewStatus(currentCrew);

			courierRepo.disconnectAllOldCouriers(connection,id);
			transportRepo.disconnectTransportOfCrew(connection,id);
			crewRepo.deleteCrewById(connection,id);

			connection.commit();
		}

		return SUCCESSFULLY_DELETE;
	}


	@Transactional
	public CrewDto createNewCrew(CrewDto request) {
		CrewDto result = new CrewDto();
		try(Connection connection = sql2o.beginTransaction()){
			List<Courier> couriers = request.getCouriers();
			checkCouriersIsExist(couriers);

			//TODO Можно сделать валидатор на проверку соответствия
			// курьеров из запроса и курьеров с теми же ИД из БД +
			// проверить, нету ли этих курьеров в других экипажах но потом

			VehicleCrew newCrew = new VehicleCrew().setStatus(CrewStatus.READY_FOR_RIDE).setId(null);
			crewRepo.addNewCrew(connection,newCrew);
			addNewCouriersToCrew(connection,newCrew.getId(),couriers);

			result.setId(newCrew.getId()).setStatus(newCrew.getStatus()).setCouriers(couriers);
			connection.commit();
		}

		return result;
	}

	private void checkCouriersIsExist(List<Courier> couriers) {
		for(Courier courier: couriers){
			if(courier.getId()==null||courier.getId()==0L){
				throw new WrongCourierException(COURIER_NOT_VALID);
			}
		}
	}

	private void checkCrewStatus(VehicleCrew oldVersion, CrewStatus status) {
		if(oldVersion.getStatus()==status) {
			return;
		}

		if (oldVersion.getStatus() == CrewStatus.ON_RIDE) {
			throw new WrongParameterException(WRONG_STATUS_OF_CREW);
		}
	}

	private void checkCrewStatus(VehicleCrew currentCrew) {
		if (currentCrew.getStatus() == CrewStatus.ON_RIDE) {
			throw new WrongParameterException(WRONG_STATUS_OF_CREW);
		}
	}

	private Page<CrewDto> createPageFromCrews(Connection connection, List<VehicleCrew> crews) {
		List<CrewDto> result = crews.stream()
				.map(currentCrew ->{
					List<Courier> couriers = courierRepo.getCouriersByCrewId(connection,currentCrew.getId());
					return new CrewDto()
							.setId(currentCrew.getId())
							.setCouriers(couriers)
							.setStatus(currentCrew.getStatus());
				})
				.collect(Collectors.toList());

		return new Page<CrewDto>().setItems(result).setTotalCount(result.size());

	}

	private CrewDto createCrew(Connection connection, VehicleCrew crewFromDB) {
		checkCrewOnNullable(crewFromDB);
		return new CrewDto()
				.setId(crewFromDB.getId())
				.setCouriers(courierRepo.getCouriersByCrewId(connection,crewFromDB.getId()))
				.setStatus(crewFromDB.getStatus());
	}

	private void addNewCouriersToCrew(Connection connection, Long crewId, List<Courier> couriers) {
		for(Courier currentCourier: couriers){
			courierRepo.addCouriersToCrew(connection,crewId,currentCourier.getId());
		}
	}

	//TODO вместо нескольких запросов в базу можно подумать, как обойтись одним, возвращающим один Boolean
	private void checkAllCouriersFromNewVersion(Connection connection, List<Courier> couriers, Long crewId) {
		boolean allCouriersCanRide = couriers.stream()
				.map(Courier::getId)
				.allMatch(id -> courierRepo.checkCouriersStatus(connection,id,crewId));

		if(!allCouriersCanRide){
			throw new WrongCourierException(COURIER_IS_BUSY);
		}
	}

	private void checkCrewOnNullable(VehicleCrew currentCrew) {
		if(currentCrew==null){
			throw new WrongCrewException(CREW_NOT_EXIST);
		}
	}
}
