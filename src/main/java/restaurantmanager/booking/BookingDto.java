package restaurantmanager.booking;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
@ToString
@EqualsAndHashCode
public class BookingDto {
	
	private Long id;
	private LocalDate bookingDate;
	private LocalTime bookingTime;
	private LocalDateTime createdAt;
	private Long boardId;
	private Long employeeId;
	private String personalData;
	private String phoneNumber;
	private String description;
}
