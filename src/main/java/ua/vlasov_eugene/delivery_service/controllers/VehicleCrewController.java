package ua.vlasov_eugene.delivery_service.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.vlasov_eugene.delivery_service.dtos.OldAndNewVersionCrewDto;
import ua.vlasov_eugene.delivery_service.dtos.CrewDto;
import ua.vlasov_eugene.delivery_service.enums.RouteStatus;
import ua.vlasov_eugene.delivery_service.services.CrewService;
import ua.vlasov_eugene.delivery_service.utils.Page;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/crew")
public class VehicleCrewController {
	private final CrewService crewService;

	@ApiOperation("Эндпоинт для получения экипажей согласно условиям")
	@GetMapping("/{routeStatus}")
	public Page<CrewDto> getCrewsByFilter(@ApiParam(value = "Статус, говорящий о состоянии экипажа\n" +
			"При пустом параметре будут переданы все экипажи")
												@PathVariable(required = false) RouteStatus routeStatus,
										  @RequestParam(defaultValue = "1") Long numberOfPage,
										  @RequestParam(defaultValue = "10") Long elementsInPage){
		return crewService.getCrewByFilter(routeStatus, numberOfPage, elementsInPage);
	}

	@ApiOperation("Создает новый экипаж из кодов Курьеров")
	@PostMapping("/new")
	public CrewDto createNewCrew (@RequestBody List<String> couriersCode){
		return crewService.createNewCrew(couriersCode);
	}

	@ApiOperation("Возвращает экипаж по id")
	@GetMapping("/{id}")
	public CrewDto getCrewById(@PathVariable Long id){
		return crewService.getCrewById(id);
	}

	@ApiOperation("Эндпоинт для прикрепления экипажа к ТС, изменения состава экипажа." +
			"Если экипаж уже в поездке, - будет выброшено исключение")
	@PutMapping
	public OldAndNewVersionCrewDto updateCrew(@RequestBody CrewDto oldVersionOfCrew){
		return crewService.updateCrew(oldVersionOfCrew);
	}

	@ApiOperation("Эндпоинт для удаления экипажа по id")
	@DeleteMapping("/{id}")
	public String deleteCrewById(@PathVariable Long id){
		return crewService.deleteById(id);
	}
}
