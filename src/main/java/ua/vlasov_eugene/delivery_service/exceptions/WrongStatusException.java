package ua.vlasov_eugene.delivery_service.exceptions;

public class WrongStatusException extends RuntimeException {
	public WrongStatusException(String message) {
		super(message);
	}
}
