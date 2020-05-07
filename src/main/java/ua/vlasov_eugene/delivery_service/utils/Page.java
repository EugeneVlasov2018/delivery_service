package ua.vlasov_eugene.delivery_service.utils;

import lombok.Data;

import java.util.List;

@Data
public class Page<T> {
	private List<T> items;
	private long totalCount;
}
