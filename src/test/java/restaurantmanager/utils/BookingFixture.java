package restaurantmanager.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static restaurantmanager.utils.RandomUtilsFixture.createRandomLong;
import static restaurantmanager.utils.RandomUtilsFixture.createRandomString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import restaurantmanager.booking.Booking;
import restaurantmanager.booking.BookingDto;
import restaurantmanager.booking.ModifyBookingDto;

public abstract class BookingFixture {
	
	public static Booking createBookingEntityWithNulls() {
		return Booking.builder().build();
	}
	
	public static Booking createBookingEntity(final Long id) {
		return Booking.builder()
				.id(id)
				.bookingDate(LocalDate.now().plusDays(10L))
				.bookingTime(LocalTime.of(17, 30))
				.createdAt(LocalDateTime.now())
				.boardId(createRandomLong())
				.employeeId(createRandomLong())
				.personalData(createRandomString())
				.phoneNumber(createRandomString())
				.description(createRandomString())
				.build();
	}
	
	public static Booking createBookingEntityFromModifyDto(final Long id, final ModifyBookingDto modifyBookingDto) {
		return Booking.builder()
				.id(id)
				.bookingDate(modifyBookingDto.getBookingDate())
				.bookingTime(modifyBookingDto.getBookingTime())
				.createdAt(modifyBookingDto.getCreatedAt())
				.boardId(modifyBookingDto.getBoardId())
				.employeeId(modifyBookingDto.getEmployeeId())
				.personalData(modifyBookingDto.getPersonalData())
				.phoneNumber(modifyBookingDto.getPhoneNumber())
				.description(modifyBookingDto.getDescription())
				.build();
	}
	
	public static ModifyBookingDto createModifyBookingDtoWithNulls() {
		return ModifyBookingDto.builder().build();
	}
	
	public static ModifyBookingDto createModifyBookingDto() {
		return ModifyBookingDto.builder()
				.bookingDate(LocalDate.now().plusDays(10L))
				.bookingTime(LocalTime.of(17, 30))
				.createdAt(LocalDateTime.now())
				.boardId(createRandomLong())
				.employeeId(createRandomLong())
				.personalData(createRandomString())
				.phoneNumber(createRandomString())
				.description(createRandomString())
				.build();
	}
	
	public static void assertBooking(final BookingDto result, final ModifyBookingDto modifyBookingDto) {
		assertThat(result.getBookingDate()).isEqualTo(modifyBookingDto.getBookingDate());
		assertThat(result.getBookingTime()).isEqualTo(modifyBookingDto.getBookingTime());
		assertThat(result.getCreatedAt()).isEqualTo(modifyBookingDto.getCreatedAt());
		assertThat(result.getBoardId()).isEqualTo(modifyBookingDto.getBoardId());
		assertThat(result.getEmployeeId()).isEqualTo(modifyBookingDto.getEmployeeId());
		assertThat(result.getPersonalData()).isEqualTo(modifyBookingDto.getPersonalData());
		assertThat(result.getPhoneNumber()).isEqualTo(modifyBookingDto.getPhoneNumber());
		assertThat(result.getDescription()).isEqualTo(modifyBookingDto.getDescription());
	}
}
