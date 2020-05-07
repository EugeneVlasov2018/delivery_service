package ua.vlasov_eugene.delivery_service.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(value = {WrongStatusException.class, WrongCourierException.class})
	public ResponseEntity<ErrorMessage> handleException(RuntimeException ex) {
		return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@Data
	@AllArgsConstructor
	private static class ErrorMessage {
		private String exceptionMessage;
	}
}
