package ua.vlasov_eugene.delivery_service.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.vlasov_eugene.delivery_service.dtos.RouteDto;
import ua.vlasov_eugene.delivery_service.entities.Route;
import ua.vlasov_eugene.delivery_service.services.RouteService;
import ua.vlasov_eugene.delivery_service.utils.Page;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/route")
public class RouteController {
	private final RouteService routeService;

	@ApiOperation("Возвращает все маршруты согласно входящих параметров.")
	@GetMapping
	public Page<RouteDto> getAllRoutes(@RequestParam(defaultValue = "1") Long numberOfPage,
									   @RequestParam(defaultValue = "10") Long elementsInPage){
		return routeService.getAllRoutes(numberOfPage,elementsInPage);
	}

	@ApiOperation("Возвращает определенный маршрут по id.")
	@GetMapping(value = "/{routeId}")
	public RouteDto getRouteById(@PathVariable Long routeId){
		return routeService.getRouteById(routeId);
	}

	@ApiOperation("Эндпоинт для создания нового маршрута")
	@PostMapping
	public RouteDto createNewRoute(@RequestBody RouteDto params){
		return routeService.createNewRoute(params);
	}

	@ApiOperation("Эндпоинт для запуска маршрута по id")
	@PutMapping(value = "/start/{id}")
	public RouteDto startRoute(@PathVariable Long id){
		return routeService.startRoute(id);
	}

	@ApiOperation("Эндпоинт для завершения маршрута по id")
	@PutMapping(value = "/finish/{id}")
	public RouteDto finishRoute(@PathVariable Long id){
		return routeService.finishRoute(id);
	}
}
