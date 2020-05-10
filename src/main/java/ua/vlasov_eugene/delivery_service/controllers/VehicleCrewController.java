package ua.vlasov_eugene.delivery_service.controllers;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.vlasov_eugene.delivery_service.dtos.CrewDto;
import ua.vlasov_eugene.delivery_service.services.CrewService;
import ua.vlasov_eugene.delivery_service.utils.Page;

import java.sql.SQLException;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/crew")
public class VehicleCrewController {
	private final CrewService crewService;

	@ApiOperation("Эндпоинт для получения экипажей")
	@GetMapping
	public Page<CrewDto> getCrewsByFilter(@RequestParam(defaultValue = "1") Long numberOfPage,
										  @RequestParam(defaultValue = "10") Long elementsInPage){
		return crewService.getCrewByFilter(numberOfPage, elementsInPage);
	}

	@ApiOperation("Создает новый экипаж")
	@PostMapping(value = "/new")
	public CrewDto createNewCrew(@RequestBody CrewDto newCrew) {
		return crewService.createNewCrew(newCrew);
	}

	@ApiOperation("Возвращает экипаж по id.\n В случае передачи несуществующего ID будет выброшено исключение")
	@GetMapping(value = "/{id}")
	public CrewDto getCrewById(@PathVariable Long id) throws SQLException {
		return crewService.getCrewById(id);
	}

	@ApiOperation("Эндпоинт для изменения состава экипажа." +
			"Если экипаж уже в поездке, - будет выброшено исключение")
	@PutMapping
	public CrewDto updateCrew(@RequestBody CrewDto oldVersionOfCrew) {
		return crewService.updateCrew(oldVersionOfCrew);
	}

	@ApiOperation("Эндпоинт для удаления экипажа по id\n В случае передачи несуществующего ID будет выброшено исключение")
	@DeleteMapping(value = "/{id}")
	public String deleteCrewById(@PathVariable Long id) {
		return crewService.deleteById(id);
	}
}
