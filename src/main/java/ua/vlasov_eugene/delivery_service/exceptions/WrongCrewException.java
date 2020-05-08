package ua.vlasov_eugene.delivery_service.exceptions;

public class WrongCrewException extends RuntimeException {
	public WrongCrewException(String message) {
		super(message);
	}
}
