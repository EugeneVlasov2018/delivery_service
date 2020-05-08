package ua.vlasov_eugene.delivery_service.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.vlasov_eugene.delivery_service.dtos.OldAndNewRouteDto;
import ua.vlasov_eugene.delivery_service.enums.RouteStatus;
import ua.vlasov_eugene.delivery_service.dtos.RouteDto;
import ua.vlasov_eugene.delivery_service.services.RouteService;
import ua.vlasov_eugene.delivery_service.utils.Page;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/route")
public class RouteController {
	private final RouteService routeService;

	@ApiOperation("Возвращает все маршруты согласно входящих параметров.")
	@GetMapping("/{routeStatus}/{clientId}")
	public Page<RouteDto> getAllRoutes(@ApiParam(value = "Параметр для отбора маршрутов по типу.\n" +
			"В случае отсутствия параметра в запросе будут получены все маршруты")
									@PathVariable(required = false) RouteStatus routeStatus,
									   @ApiParam(value = "Параметр для отбора маршрута по клиенту.\n" +
											"В случае отсутствия будут переданы маршруты по всем клиентам")
									@PathVariable(required = false) String clientId,
									   @RequestParam(defaultValue = "1") Long numberOfPage,
									   @RequestParam(defaultValue = "10") Long elementsInPage){
		return routeService.getAllRoutes(routeStatus, clientId,numberOfPage,elementsInPage);
	}

	@ApiOperation("Возвращает определенный маршрут по id.")
	@GetMapping("/{routeId}")
	public RouteDto getRouteById(@PathVariable Long routeId){
		return routeService.getRouteById(routeId);
	}

	@ApiOperation("Эндпоинт для создания нового маршрута")
	@PostMapping
	public RouteDto createNewRoute(@RequestBody RouteDto params){
		return routeService.createNewRoute(params);
	}

	@ApiOperation("Эндпоинт для изменения маршрута.\n" +
			"Если статус маршрута любой, кроме FUTURE_ROUTE, будет выброшено исключение")
	@PutMapping
	public OldAndNewRouteDto updateRouteById(@RequestBody RouteDto route){
		return routeService.updateRouteById(route);
	}

	@ApiOperation("Эндпоинт для удаления маршрута по id.\n" +
			"Если статус маршрута ROUTE_IN_PROCESS, будет выброшено исключение")
	@DeleteMapping("/{routeId}")
	public String deleteRouteById(@PathVariable Long routeId){
		return routeService.deleteRouteById(routeId);
	}

	@ApiOperation("Эндпоинт для запуска маршрута по id")
	@PutMapping("/start/{id}")
	public RouteDto startRoute(@PathVariable Long id){
		return routeService.startRoute(id);
	}

	@ApiOperation("Эндпоинт для завершения маршрута по id")
	@PutMapping("/finish/{id}")
	public RouteDto finishRoute(@PathVariable Long id){
		return routeService.finishRoute(id);
	}
}
