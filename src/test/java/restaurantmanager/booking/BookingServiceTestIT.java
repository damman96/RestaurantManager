package restaurantmanager.booking;

import static java.util.stream.Collectors.toUnmodifiableList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static restaurantmanager.utils.BookingFixture.assertBooking;
import static restaurantmanager.utils.BookingFixture.createBookingEntity;
import static restaurantmanager.utils.BookingFixture.createBookingEntityWithNulls;
import static restaurantmanager.utils.BookingFixture.createModifyBookingDto;
import static restaurantmanager.utils.BookingFixture.createModifyBookingDtoWithNulls;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import restaurantmanager.NotFoundException;
import restaurantmanager.utils.BookingFixture;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BookingServiceTestIT {
	
	@Autowired
	private BookingDao bookingDao;
	
	@Autowired
	private BookingService bookingService;
	
	@BeforeEach
	void setUp() {
		this.bookingDao.deleteAll();
	}
	
	@Test
	void getAllBookings_Should_ReturnEmptyList_When_EntitiesAreNotPresentInDb() {
		// when
		final var result = this.bookingService.getAllBookings();
		
		// then
		assertThat(result).isEmpty();
	}
	
	@Test
	void getAllBookings_Should_ReturnResultList_When_EntitiesArePresentInDb() {
		// given
		final var bookingsToAdd = List.of(createBookingEntity(1L),
										  createBookingEntity(2L),
										  createBookingEntity(3L));
		this.bookingDao.saveAll(bookingsToAdd);
		
		// when
		final var result = this.bookingService.getAllBookings();
		
		// then
		assertThat(result).isNotEmpty();
		assertThat(result).extracting(BookingDto::getId)
				.containsAll(bookingsToAdd.stream().map(Booking::getId).collect(toUnmodifiableList()));
		assertThat(result).extracting(BookingDto::getBookingDate)
				.containsAll(bookingsToAdd.stream().map(Booking::getBookingDate).collect(toUnmodifiableList()));
		assertThat(result).extracting(BookingDto::getBookingTime)
				.containsAll(bookingsToAdd.stream().map(Booking::getBookingTime).collect(toUnmodifiableList()));
		assertThat(result).extracting(BookingDto::getBoardId)
				.containsAll(bookingsToAdd.stream().map(Booking::getBoardId).collect(toUnmodifiableList()));
		assertThat(result).extracting(BookingDto::getEmployeeId)
				.containsAll(bookingsToAdd.stream().map(Booking::getEmployeeId).collect(toUnmodifiableList()));
		assertThat(result).extracting(BookingDto::getPersonalData)
				.containsAll(bookingsToAdd.stream().map(Booking::getPersonalData).collect(toUnmodifiableList()));
		assertThat(result).extracting(BookingDto::getPhoneNumber)
				.containsAll(bookingsToAdd.stream().map(Booking::getPhoneNumber).collect(toUnmodifiableList()));
		assertThat(result).extracting(BookingDto::getDescription)
				.containsAll(bookingsToAdd.stream().map(Booking::getDescription).collect(toUnmodifiableList()));
	}
	
	@Test
	void getBookingById_Should_ReturnResult_When_EntityExists() {
		// given
		final var booking = createBookingEntity(1L);
		this.bookingDao.save(booking);
		
		// when
		final var result = this.bookingService.getBookingById(booking.getId());
		
		// then
		assertThat(result)
				.usingRecursiveComparison()
				.ignoringFields("createdAt")
				.isEqualTo(BookingMapper.INSTANCE.map(booking));
	}
	
	@Test
	void getBookingById_Should_ThrowNotFoundException_When_EntityWithGivenIdNotExist() {
		// given
		final var id = new Random().nextLong();
		
		// when
		final var throwable = catchThrowable(() -> this.bookingService.getBookingById(id));
		
		// then
		assertThat(throwable).isInstanceOf(NotFoundException.class)
				.hasMessage("Booking with id=" + id + " not found");
	}
	
	@Test
	void addBooking_Should_SaveEntity() {
		// given
		final var bookingToAdd = createModifyBookingDto();
		
		// when
		final var result = this.bookingService.addBooking(bookingToAdd);
		
		// then
		assertThat(result.getId()).isNotNull().isPositive();
		BookingFixture.assertBooking(result, bookingToAdd);
	}
	
	@Test
	void updateBooking_Should_UpdateBooking() {
		// given
		final var existingBooking = createBookingEntity(1L);
		this.bookingDao.save(existingBooking);
		
		final var bookingToUpdate = createModifyBookingDto();
		
		// when
		final var result = this.bookingService.updateBooking(existingBooking.getId(), bookingToUpdate);
		
		// then
		assertThat(result.getId()).isEqualTo(existingBooking.getId());
		assertBooking(result, bookingToUpdate);
	}
	
	@Test
	void updateBooking_Should_ThrowNotFoundException_When_EntityExists() {
		// given
		final var id = new Random().nextLong();
		
		// when
		final var throwable = catchThrowable(() -> this.bookingService.updateBooking(id, createModifyBookingDtoWithNulls()));
		
		// then
		assertThat(throwable).isInstanceOf(NotFoundException.class)
				.hasMessage("Booking with id=" + id + " not found");
	}
	
	@Test
	void deleteBookingById_Should_RemoveEntity_When_EntityExists() {
		// given
		final var booking = createBookingEntityWithNulls();
		this.bookingDao.save(booking);
		
		// when
		this.bookingService.deleteBookingById(booking.getId());
		
		//then
		assertThat(this.bookingDao.findById(booking.getId())).isNotPresent();
	}
	
	@Test
	void deleteBookingById_Should_ThrowNotFoundException_When_EntityExists() {
		// given
		final var id = new Random().nextLong();
		
		// when
		final var throwable = catchThrowable(() -> this.bookingService.deleteBookingById(id));
		
		// then
		assertThat(throwable).isInstanceOf(NotFoundException.class)
				.hasMessage("Booking with id=" + id + " not found");
	}
	
}