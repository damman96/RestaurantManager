package restaurantmanager.utils;

import static restaurantmanager.utils.RandomUtilsFixture.createRandomLong;
import static restaurantmanager.utils.RandomUtilsFixture.createRandomString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import restaurantmanager.booking.Booking;
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
}
