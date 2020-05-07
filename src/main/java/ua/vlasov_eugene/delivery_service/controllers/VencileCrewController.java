package ua.vlasov_eugene.delivery_service.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.vlasov_eugene.delivery_service.dtos.OldAndNewVersionCrewDto;
import ua.vlasov_eugene.delivery_service.entityes.VencileCrew;
import ua.vlasov_eugene.delivery_service.enums.CrewStatus;
import ua.vlasov_eugene.delivery_service.services.CrewService;
import ua.vlasov_eugene.delivery_service.utils.Page;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/crew")
public class VencileCrewController {
	private final CrewService crewService;

	@ApiOperation("Эндпоинт для получения экипажей согласно условиям")
	@GetMapping("/{crewStatus}")
	public Page<VencileCrew> getCrewsByFilter(@ApiParam(value = "Статус экипажей.\n" +
			"При пустом параметре будут переданы все экипажи")
												@PathVariable(required = false) CrewStatus crewStatus,
											  @RequestParam(defaultValue = "0") Long numberOfPage,
											  @RequestParam(defaultValue = "10") Long elementsInPage){
		return crewService.getCrewByFilter(crewStatus, numberOfPage, elementsInPage);
	}

	@ApiOperation("Создает новый экипаж из кодов Курьеров")
	@PostMapping("/new")
	public VencileCrew createNewCrew (@RequestBody List<String> couriersCode){
		return crewService.createNewCrew(couriersCode);
	}

	@ApiOperation("Возвращает экипаж по id")
	@GetMapping("/{id}")
	public VencileCrew getCrewById(@PathVariable Long id){
		return crewService.getCrewById(id);
	}

	@ApiOperation("Эндпоинт для прикрепления экипажа к ТС, изменения состава экипажа")
	@PutMapping
	public OldAndNewVersionCrewDto updateCrew(@RequestBody VencileCrew oldVersionOfCrew){
		return crewService.updateCrew(oldVersionOfCrew);
	}

	@ApiOperation("Эндпоинт для удаления сотава по id")
	@DeleteMapping("/{id}")
	public String deleteCrewById(@PathVariable Long id){
		return crewService.deleteById(id);
	}
}
