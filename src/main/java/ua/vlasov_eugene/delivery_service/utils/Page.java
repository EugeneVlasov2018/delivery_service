package ua.vlasov_eugene.delivery_service.utils;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class Page<T> {
	private List<T> items;
	private long totalCount;
}
