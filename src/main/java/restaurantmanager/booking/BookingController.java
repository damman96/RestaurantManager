package restaurantmanager.booking;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
public class BookingController {
	
	private final BookingService bookingService;
	
	public BookingController(final BookingService bookingService) {
		this.bookingService = bookingService;
	}
	
	@GetMapping
	public ResponseEntity<List<BookingDto>> getAllBookings() {
		return ResponseEntity.ok(this.bookingService.getAllBookings());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<BookingDto> getBookingById(@PathVariable final Long id) {
		return ResponseEntity.ok(this.bookingService.getBookingById(id));
	}
	
	@PostMapping
	public ResponseEntity<BookingDto> addBooking(@RequestBody final ModifyBookingDto modifyBookingDto) {
		return ResponseEntity.ok(this.bookingService.addBooking(modifyBookingDto));
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<BookingDto> updateBooking(@PathVariable final Long id,
													@RequestBody final ModifyBookingDto modifyBookingDto) {
		return ResponseEntity.ok(this.bookingService.updateBooking(id, modifyBookingDto));
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<BookingDto> deleteBookingById(@PathVariable final Long id) {
		return ResponseEntity.ok(this.bookingService.deleteBookingById(id));
		
	}
}
