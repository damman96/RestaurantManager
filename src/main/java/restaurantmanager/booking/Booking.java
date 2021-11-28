package restaurantmanager.booking;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PACKAGE)
@Getter
@ToString
@EqualsAndHashCode
@Table(name = "bookings")
public class Booking {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "booking_date")
	private LocalDate bookingDate;
	
	@Column(name = "booking_time")
	private LocalTime bookingTime;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@Column(name = "board_id")
	private Long boardId;
	
	@Column(name = "employee_id")
	private Long employeeId;
	
	@Column(name = "personal_data")
	private String personalData;
	
	@Column(name = "phone_number")
	private String phoneNumber;
	
	private String description;
	
}
