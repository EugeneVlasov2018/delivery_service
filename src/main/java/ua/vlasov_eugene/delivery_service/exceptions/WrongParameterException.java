package ua.vlasov_eugene.delivery_service.exceptions;

public class WrongParameterException extends RuntimeException {
	public WrongParameterException(String message) {
		super(message);
	}
}
