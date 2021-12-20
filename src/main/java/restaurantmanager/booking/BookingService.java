package restaurantmanager.booking;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import restaurantmanager.NotFoundException;

@Slf4j
@Service
public class BookingService {
	
	private final BookingDao bookingDao;
	
	public BookingService(final BookingDao bookingDao) {
		this.bookingDao = bookingDao;
	}
	
	List<BookingDto> getAllBookings() {
		final var bookings = this.bookingDao.findAll()
				.stream()
				.map(BookingMapper.INSTANCE::map)
				.collect(toUnmodifiableList());
		log.info("Received bookings={}", bookings);
		return bookings;
	}
	
	BookingDto getBookingById(final Long id) {
		final var receivedBooking = BookingMapper.INSTANCE.map(this.getEntityFromDb(id));
		log.info("Received booking={}", receivedBooking);
		return receivedBooking;
	}
	
	BookingDto addBooking(final ModifyBookingDto modifyBookingDto) {
		final var savedBooking = this.bookingDao.save(BookingMapper.INSTANCE.mapFromModify(modifyBookingDto));
		log.info("Saved booking={}", savedBooking);
		return BookingMapper.INSTANCE.map(savedBooking);
	}
	
	BookingDto updateBooking(final Long id, final ModifyBookingDto modifyBookingDto) {
		final var bookingFromDb = this.getEntityFromDb(id);
		final var modifiedBooking = Booking.builder()
				.id(bookingFromDb.getId())
				.bookingDate(modifyBookingDto.getBookingDate())
				.bookingTime(modifyBookingDto.getBookingTime())
				.createdAt(modifyBookingDto.getCreatedAt())
				.boardId(modifyBookingDto.getBoardId())
				.employeeId(modifyBookingDto.getEmployeeId())
				.personalData(modifyBookingDto.getPersonalData())
				.phoneNumber(modifyBookingDto.getPhoneNumber())
				.description(modifyBookingDto.getDescription())
				.build();
		return BookingMapper.INSTANCE.map(this.bookingDao.save(modifiedBooking));
	}
	
	BookingDto deleteBookingById(final Long id) {
		final var removedBooking = this.getBookingById(id);
		this.bookingDao.deleteById(removedBooking.getId());
		log.info("Removed booking={}", removedBooking);
		return removedBooking;
	}
	
	private Booking getEntityFromDb(final Long id) {
		return this.bookingDao.findById(id)
				.orElseThrow(() -> new NotFoundException("Booking with id=" + id + " not found"));
	}
}
