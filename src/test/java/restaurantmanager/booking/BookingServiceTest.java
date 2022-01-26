package restaurantmanager.booking;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static restaurantmanager.utils.BookingFixture.assertBooking;
import static restaurantmanager.utils.BookingFixture.createBookingEntity;
import static restaurantmanager.utils.BookingFixture.createBookingEntityFromModifyDto;
import static restaurantmanager.utils.BookingFixture.createModifyBookingDto;
import static restaurantmanager.utils.BookingFixture.createModifyBookingDtoWithNulls;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import restaurantmanager.NotFoundException;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
	
	private final BookingDao bookingDao = Mockito.mock(BookingDao.class);
	
	@InjectMocks
	private BookingService bookingService;
	
	@BeforeEach
	void setUp() {
		this.bookingDao.deleteAll();
	}
	
	@Test
	void getAllBookings_Should_ReturnEmptyList_When_EntitiesAreNotPresentInDb() {
		// given
		when(this.bookingDao.findAll()).thenReturn(emptyList());
		
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
		
		when(this.bookingDao.findAll()).thenReturn(bookingsToAdd);
		
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
		final var id = 1L;
		final var booking = createBookingEntity(id);
		when(this.bookingDao.findById(id)).thenReturn(Optional.of(booking));
		
		// when
		final var result = this.bookingService.getBookingById(id);
		
		// then
		assertThat(result).isEqualTo(BookingMapper.INSTANCE.map(booking));
	}
	
	@Test
	void getBookingById_Should_ThrowNotFoundException_When_EntityWithGivenIdNotExist() {
		// given
		final var id = new Random().nextLong();
		when(this.bookingDao.findById(id)).thenReturn(Optional.empty());
		
		// when
		final var throwable = catchThrowable(() -> this.bookingService.getBookingById(id));
		
		// then
		assertThat(throwable).isInstanceOf(NotFoundException.class)
				.hasMessage("Booking with id=" + id + " not found");
	}
	
	@Test
	void addBooking_Should_SaveEntity() {
		// given
		final var id = 1L;
		final var bookingToAdd = createModifyBookingDto();
		
		// when
		final var entity = createBookingEntityFromModifyDto(id, bookingToAdd);
		
		when(this.bookingDao.save(BookingMapper.INSTANCE.mapFromModify(bookingToAdd))).thenReturn(entity);
		final var result = this.bookingService.addBooking(bookingToAdd);
		
		// then
		assertThat(result.getId()).isNotNull().isPositive();
		assertBooking(result, bookingToAdd);
	}
	
	@Test
	void updateBooking_Should_UpdateBooking() {
		// given
		final var id = 1L;
		final var existingBooking = createBookingEntity(id);
		when(this.bookingDao.findById(id)).thenReturn(Optional.of(existingBooking));
		
		final var bookingToUpdate = createModifyBookingDto();
		
		// when
		final var entity = createBookingEntityFromModifyDto(id, bookingToUpdate);
		
		when(this.bookingDao.save(entity)).thenReturn(entity);
		final var result = this.bookingService.updateBooking(id, bookingToUpdate);
		
		// then
		assertThat(result.getId()).isEqualTo(id);
		assertBooking(result, bookingToUpdate);
	}
	
	@Test
	void updateBooking_Should_ThrowNotFoundException_When_EntityExists() {
		// given
		final var id = new Random().nextLong();
		when(this.bookingDao.findById(id)).thenReturn(Optional.empty());
		
		// when
		final var throwable = catchThrowable(() -> this.bookingService.updateBooking(id, createModifyBookingDtoWithNulls()));
		
		// then
		assertThat(throwable).isInstanceOf(NotFoundException.class)
				.hasMessage("Booking with id=" + id + " not found");
	}
	
	@Test
	void deleteBookingById_Should_RemoveEntity_When_EntityExists() {
		// given
		final var id = 1L;
		final var booking = createBookingEntity(id);
		when(this.bookingDao.findById(id)).thenReturn(Optional.of(booking));
		
		// when
		this.bookingService.deleteBookingById(id);
		
		//then
		verify(this.bookingDao, times(1)).deleteById(id);
	}
	
	@Test
	void deleteBookingById_Should_ThrowNotFoundException_When_EntityExists() {
		// given
		final var id = new Random().nextLong();
		when(this.bookingDao.findById(id)).thenReturn(Optional.empty());
		
		// when
		final var throwable = catchThrowable(() -> this.bookingService.deleteBookingById(id));
		
		// then
		assertThat(throwable).isInstanceOf(NotFoundException.class)
				.hasMessage("Booking with id=" + id + " not found");
	}
	
}
