package restaurantmanager;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = -8356043735157021950L;
	
	public NotFoundException(final String message) {
		super(message);
	}
}
