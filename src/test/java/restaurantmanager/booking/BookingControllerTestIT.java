package restaurantmanager.booking;

import static java.util.stream.Collectors.toUnmodifiableList;
import static org.assertj.core.api.Assertions.assertThat;
import static restaurantmanager.utils.BookingFixture.createBookingEntityWithNulls;
import static restaurantmanager.utils.BookingFixture.createModifyBookingDto;
import static restaurantmanager.utils.BookingFixture.createModifyBookingDtoWithNulls;
import static restaurantmanager.utils.RandomUtilsFixture.createRandomLong;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BookingControllerTestIT {
	
	private static final String HTTP_LOCAL_HOST = "http://localhost:";
	
	private static final String SLASH = "/";
	
	private static final String BOOKINGS = "bookings";
	private static final String UPDATE = "update";
	private static final String DELETE = "delete";
	
	@Autowired
	private BookingDao bookingDao;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@LocalServerPort
	private int randomServerPort;
	
	@BeforeEach
	void setUp() {
		this.bookingDao.deleteAll();
	}
	
	@Test
	void getAllBookings_Should_ReturnStatusCode200AndEmptyList_When_DatabaseIsEmpty() {
		// given
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + BOOKINGS;
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.GET,
				HttpEntity.EMPTY,
				new ParameterizedTypeReference<List<BookingDto>>() {
				});
		
		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotNull().isEmpty();
	}
	
	@Test
	void getAllBookings_Should_ReturnStatusCode200AndResultList_When_DatabaseIsNotEmpty() {
		//given
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + BOOKINGS;
		final var bookings = List.of(
				createBookingEntityWithNulls(),
				createBookingEntityWithNulls(),
				createBookingEntityWithNulls());
		this.bookingDao.saveAll(bookings);
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.GET,
				HttpEntity.EMPTY,
				new ParameterizedTypeReference<List<BookingDto>>() {
				});
		
		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotNull().isNotEmpty();
		assertThat(result.getBody()).usingRecursiveComparison()
				.isEqualTo(bookings.stream().map(BookingMapper.INSTANCE::map).collect(toUnmodifiableList()));
	}
	
	@Test
	void getBookingById_Should_ReturnStatusCode200AndResult_When_EntityWithGivenIdExists() {
		// given
		final var saved = this.bookingDao.save(createBookingEntityWithNulls());
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + BOOKINGS + SLASH + saved.getId();
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.GET,
				HttpEntity.EMPTY,
				BookingDto.class);
		
		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody())
				.isNotNull()
				.isEqualTo(BookingMapper.INSTANCE.map(saved));
	}
	
	@Test
	void getBookingById_Should_ReturnStatusCode404_When_EntityWithGivenIdNotExist() {
		// given
		final var id = createRandomLong();
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + BOOKINGS + SLASH + id;
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.GET,
				HttpEntity.EMPTY,
				BookingDto.class);
		
		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	@Test
	void addBooking_Should_ReturnStatusCode200AndResult_When_SuccessfullyAddedBooking() {
		// given
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + BOOKINGS;
		final var bookingToAdd = createModifyBookingDtoWithNulls();
		final var body = new HttpEntity<>(bookingToAdd);
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.POST,
				body,
				BookingDto.class);
		
		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getId()).isNotNull().isPositive();
		assertThat(result.getBody().getBookingDate()).isEqualTo(bookingToAdd.getBookingDate());
		assertThat(result.getBody().getBookingTime()).isEqualTo(bookingToAdd.getBookingTime());
		assertThat(result.getBody().getCreatedAt()).isEqualTo(bookingToAdd.getCreatedAt());
		assertThat(result.getBody().getBoardId()).isEqualTo(bookingToAdd.getBoardId());
		assertThat(result.getBody().getEmployeeId()).isEqualTo(bookingToAdd.getEmployeeId());
		assertThat(result.getBody().getPersonalData()).isEqualTo(bookingToAdd.getPersonalData());
		assertThat(result.getBody().getPhoneNumber()).isEqualTo(bookingToAdd.getPhoneNumber());
		assertThat(result.getBody().getDescription()).isEqualTo(bookingToAdd.getDescription());
	}
	
	@Test
	void updateBooking_Should_ReturnStatusCode200_When_SuccessfullyUpdatedEntity() {
		// given
		final var saved = this.bookingDao.save(createBookingEntityWithNulls());
		final var modifyBookingDto = createModifyBookingDto();
		
		final var body = new HttpEntity<>(modifyBookingDto);
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + BOOKINGS + SLASH + UPDATE + SLASH + saved.getId();
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.PUT,
				body,
				BookingDto.class);
		
		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getId()).isEqualTo(saved.getId());
		assertThat(result.getBody().getBookingDate()).isEqualTo(modifyBookingDto.getBookingDate());
		assertThat(result.getBody().getBookingTime()).isEqualTo(modifyBookingDto.getBookingTime());
		assertThat(result.getBody().getCreatedAt()).isEqualTo(modifyBookingDto.getCreatedAt());
		assertThat(result.getBody().getBoardId()).isEqualTo(modifyBookingDto.getBoardId());
		assertThat(result.getBody().getEmployeeId()).isEqualTo(modifyBookingDto.getEmployeeId());
		assertThat(result.getBody().getPersonalData()).isEqualTo(modifyBookingDto.getPersonalData());
		assertThat(result.getBody().getPhoneNumber()).isEqualTo(modifyBookingDto.getPhoneNumber());
		assertThat(result.getBody().getDescription()).isEqualTo(modifyBookingDto.getDescription());
	}
	
	@Test
	void updateBooking_Should_ReturnStatusCode404_When_EntityWithGivenIdNotExist() {
		// given
		final var id = createRandomLong();
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + BOOKINGS + SLASH + UPDATE + SLASH + id;
		final var body = new HttpEntity<>(createModifyBookingDtoWithNulls());
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.PUT,
				body,
				BookingDto.class);
		
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	@Test
	void deleteBookingById_Should_ReturnStatusCode200AndResult_When_EntityWithGivenIdWasDeleted() {
		// given
		final var saved = this.bookingDao.save(createBookingEntityWithNulls());
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + BOOKINGS + SLASH + DELETE + SLASH + saved.getId();
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.DELETE,
				HttpEntity.EMPTY,
				BookingDto.class);
		
		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody())
				.isNotNull()
				.isEqualTo(BookingMapper.INSTANCE.map(saved));
	}
	
}