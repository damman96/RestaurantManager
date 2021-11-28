package restaurantmanager.booking;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookingDaoTest {
	
	@Autowired
	private BookingDao bookingDao;
	
	@BeforeEach
	void setUp() {
		this.bookingDao.deleteAll();
	}
	
	@Test
	void findAll_Should_ReturnEmptyResult_When_NoEntitiesArePresentInDb() {
		// when
		final var result = this.bookingDao.findAll();
		
		// then
		assertThat(result).isEmpty();
	}
	
	@Test
	void save_Should_SaveEntity() {
		// given
		final var bookingToAdd = Booking.builder().build();
		
		// when
		final var result = this.bookingDao.save(bookingToAdd);
		
		// then
		assertThat(result).isEqualTo(bookingToAdd);
	}
	
	@Test
	void findById_Should_ReturnEntity_When_EntityIsPresent() {
		// given
		final var bookingToAdd = Booking.builder().build();
		this.bookingDao.save(bookingToAdd);
		
		// when
		final var result = this.bookingDao.findById(bookingToAdd.getId());
		
		// then
		assertThat(result)
				.isPresent()
				.contains(bookingToAdd);
	}
	
	@Test
	void findById_Should_ReturnEmptyResult_When_EntityIsNotPresent() {
		// given
		final var notExistingId = new Random().nextLong();
		// when
		final var result = this.bookingDao.findById(notExistingId);
		
		// then
		assertThat(result)
				.isNotPresent();
	}
	
	@Test
	void deleteAll_Should_ReturnEmptyResult_When_AllEntitiesWasRemoved() {
		// given
		this.bookingDao.save(Booking.builder().build());
		this.bookingDao.save(Booking.builder().build());
		this.bookingDao.save(Booking.builder().build());
		
		// when
		this.bookingDao.deleteAll();
		final var result = this.bookingDao.findAll();
		
		// then
		assertThat(result).isEmpty();
	}
	
}