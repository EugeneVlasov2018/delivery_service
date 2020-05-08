package ua.vlasov_eugene.delivery_service.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ua.vlasov_eugene.delivery_service.dtos.RouteDto;
import ua.vlasov_eugene.delivery_service.entities.Route;

@Mapper(componentModel = "spring")
@Component
public interface RouteMapper {
	Route RouteDtoToRoute(RouteDto routeDto);

	RouteDto RouteToRouteDto(Route route);
}
