package restaurantmanager;

public class NotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = -8356043735157021950L;
	
	public NotFoundException(final String message) {
		super(message);
	}
}
