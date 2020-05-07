package ua.vlasov_eugene.delivery_service.exceptions;

public class WrongCourierException extends RuntimeException {
	public WrongCourierException(String message) {
		super(message);
	}
}
